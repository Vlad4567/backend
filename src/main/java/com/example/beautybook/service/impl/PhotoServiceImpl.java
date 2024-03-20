package com.example.beautybook.service.impl;

import com.example.beautybook.dto.photo.PhotoDto;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.exceptions.VirusDetectionException;
import com.example.beautybook.exceptions.photo.GalleryLimitExceededException;
import com.example.beautybook.mapper.PhotoMapper;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.model.Photo;
import com.example.beautybook.model.Subcategory;
import com.example.beautybook.repository.mastercard.MasterCardRepository;
import com.example.beautybook.repository.photo.PhotoRepository;
import com.example.beautybook.service.PhotoService;
import com.example.beautybook.util.UploadFileUtil;
import com.example.beautybook.util.VirusScannerUtil;
import com.example.beautybook.util.impl.ImageUtil;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
    private static final String IMAGE_FORMAT = ".jpg";
    private static final String IMAGE_NAME = "masterCardPhoto";
    private final VirusScannerUtil virusScanner;
    private final MasterCardRepository masterCardRepository;
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;
    private final UploadFileUtil uploadFileUtil;
    @Value("${uploud.dir}")
    private String uploadDir;
    @Value("${limit.photo}")
    private int limitPhoto;

    @Transactional
    @Override
    public PhotoDto savePhoto(MultipartFile file, Long subcategoryId) {
        MasterCard masterCard = getMasterCardAuthenticatedUser();
        if (checkSubcategoryExistsById(masterCard, subcategoryId) == null) {
            throw new EntityNotFoundException("Not found subcategory id in master card");
        }
        if (masterCard.getGallery().size() >= limitPhoto) {
            throw new GalleryLimitExceededException(
                    "Exceeded the maximum limit for gallery photos. "
                            + "You can only upload up to " + limitPhoto + " photos.");
        }
        Photo newPhoto = photoRepository.save(
                new Photo(null, "-", masterCard, subcategoryId));
        String path = uploadDir + IMAGE_NAME + newPhoto.getId();
        String newPhotoPath = uploadFileUtil.uploadFile(file, path + IMAGE_FORMAT);
        if (virusScanner.scanFile(newPhotoPath) instanceof ScanResult.VirusFound) {
            photoRepository.delete(newPhoto);
            throw new VirusDetectionException("Virus detected in the uploaded photo.");
        }
        String fileName = IMAGE_NAME + newPhoto.getId() + IMAGE_FORMAT;
        newPhoto.setPhotoUrl(fileName);
        photoRepository.save(newPhoto);
        createImageCopy(path);
        if (masterCard.getGallery().isEmpty()) {
            createMainImage(path);
            masterCard.setMainPhoto(newPhoto);
            masterCardRepository.save(masterCard);
        }
        return new PhotoDto(fileName);
    }

    @Override
    public void deletePhoto(Long id) {
        Photo photo = photoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found photo by id " + id));
        String file = photo.getPhotoUrl();
        photoRepository.delete(photo);
        if (file.contains(":")) {
            file = file.replace(":", "\\");
        }
        deleteFile(file);
    }

    @Override
    public void updateMainPhoto(Long id) {
        MasterCard masterCard = getMasterCardAuthenticatedUser();
        Photo photo = photoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found photo by id " + id));
        String fileName = masterCard.getMainPhoto().getPhotoUrl();
        masterCard.setMainPhoto(photo);
        String name = fileName.substring(0, fileName.length() - 3);
        deleteFile(uploadDir + name + "M" + IMAGE_FORMAT);
        deleteFile(uploadDir + name + "Mm" + IMAGE_FORMAT);
        createMainImage(uploadDir + name);
    }

    @Override
    public List<PhotoDto> getPhotoByMasterCardAndSubcategory(
            Long subcategoryId, Long masterCardId) {
        if (masterCardId == null) {
            masterCardId = getMasterCardAuthenticatedUser().getId();
        }
        return photoRepository
                .findAllByMasterCardIdAndSubcategoryId(masterCardId, subcategoryId).stream()
                .map(photoMapper::toDto)
                .toList();
    }

    private MasterCard getMasterCardAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return masterCardRepository.findByUserEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not fount master card by user email: "
                        + authentication.getName()
                ));
    }

    private void createImageCopy(String path) {
        ImageUtil.resizeImage(
                path + IMAGE_FORMAT,
                path + "G" + IMAGE_FORMAT,
                498, 380
        );
        ImageUtil.resizeImage(
                path + IMAGE_FORMAT,
                path + "C" + IMAGE_FORMAT,
                85,
                85
        );
        ImageUtil.applyBlur(
                path + "G" + IMAGE_FORMAT,
                path + "B" + IMAGE_FORMAT
        );
    }

    private void createMainImage(String path) {
        ImageUtil.resizeImage(
                path + IMAGE_FORMAT,
                path + "M" + IMAGE_FORMAT,
                758, 436
        );
        ImageUtil.resizeImage(
                path + IMAGE_FORMAT,
                path + "Mm" + IMAGE_FORMAT,
                270,
                200
        );
    }

    private Long checkSubcategoryExistsById(MasterCard masterCard, Long subcategoryId) {
        return masterCard.getSubcategories().stream()
                .map(Subcategory::getId).filter(id -> id.equals(subcategoryId))
                .findFirst()
                .orElse(null);
    }

    private void deleteFile(String file) {
        Path filePath = Path.of(uploadDir + file);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting the infected file: " + file, e);
        }
    }
}
