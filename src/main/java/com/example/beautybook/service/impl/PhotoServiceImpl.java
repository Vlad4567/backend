package com.example.beautybook.service.impl;

import com.example.beautybook.dto.photo.PhotoDto;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.exceptions.VirusDetectionException;
import com.example.beautybook.exceptions.photo.GalleryLimitExceededException;
import com.example.beautybook.exceptions.photo.InvalidOriginFileNameException;
import com.example.beautybook.mapper.PhotoMapper;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.model.Photo;
import com.example.beautybook.repository.MasterCardRepository;
import com.example.beautybook.repository.PhotoRepository;
import com.example.beautybook.service.PhotoService;
import com.example.beautybook.service.UploadFileService;
import com.example.beautybook.virusscanner.VirusScannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.capybara.clamav.commands.scan.result.ScanResult;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final VirusScannerService virusScanner;
    private final MasterCardRepository masterCardRepository;
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;
    private final UploadFileService uploadFileService;
    @Value("${uploud.dir}")
    private String uploadDir;
    @Value("${limit.photo}")
    private int limitPhoto;

    @Override
    public PhotoDto savePhoto(MultipartFile file) {
        MasterCard masterCard = getMasterCardAuthenticatedUser();
        if (masterCard.getGallery().size() >= limitPhoto) {
            throw new GalleryLimitExceededException(
                    "Exceeded the maximum limit for gallery photos. "
                            + "You can only upload up to " + limitPhoto + " photos.");
        }
        String path = uploadDir + generatePhotoFileName(masterCard, file);
        String newPhotoPath = uploadFileService.uploadFile(file, path);
        if (virusScanner.scanFile(newPhotoPath) instanceof ScanResult.VirusFound) {
            throw new VirusDetectionException("Virus detected in the uploaded photo.");
        }
        Photo newPhoto = new Photo(null, newPhotoPath, masterCard);
        if (masterCard.getGallery().isEmpty()) {
            masterCard.setMainPhoto(newPhoto);
        }
        masterCard.getGallery().add(newPhoto);
        masterCardRepository.save(masterCard);
        return new PhotoDto(newPhotoPath);
    }

    private MasterCard getMasterCardAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return masterCardRepository.findByUserEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not fount master card by user email: "
                        + authentication.getName()
                ));
    }

    private String generatePhotoFileName(MasterCard masterCard, MultipartFile file) {
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
            throw new InvalidOriginFileNameException("Origin file name is blank.");
        }
        String[] fileNameParts = file.getOriginalFilename().split("\\.");
        if (fileNameParts.length < 2) {
            throw new InvalidOriginFileNameException("Invalid origin file name format.");
        }
        String fileExtension = fileNameParts[fileNameParts.length - 1];
        int nextPhotoIndex = masterCard.getGallery().size() + 1;
        return "masterCardId"
                + masterCard.getId()
                + "Foto"
                + nextPhotoIndex
                + "."
                + fileExtension;
    }
}
