package com.example.beautybook.service.impl;

import com.example.beautybook.dto.photo.PhotoDto;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.exceptions.photo.GalleryLimitExceededException;
import com.example.beautybook.mapper.PhotoMapper;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.model.Photo;
import com.example.beautybook.model.ServiceCard;
import com.example.beautybook.model.Subcategory;
import com.example.beautybook.repository.mastercard.MasterCardRepository;
import com.example.beautybook.repository.photo.PhotoRepository;
import com.example.beautybook.repository.servicecard.ServiceCardRepository;
import com.example.beautybook.service.PhotoService;
import com.example.beautybook.util.UploadFileUtil;
import com.example.beautybook.util.VirusScannerUtil;
import com.example.beautybook.util.impl.ImageUtil;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private static final String IMAGE_FORMAT = ".jpg";
    private static final String IMAGE_NAME = "masterCardPhoto";
    private final VirusScannerUtil virusScanner;
    private final ServiceCardRepository serviceCardRepository;
    private final MasterCardRepository masterCardRepository;
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;
    private final UploadFileUtil uploadFileUtil;
    private final ScheduledExecutorService executor;
    @Value("${uploud.dir}")
    private String uploadDir;
    @Value("${limit.photo}")
    private int limitPhoto;

    @Transactional
    @Override
    public PhotoDto savePhoto(MultipartFile file, Long subcategoryId) {
        MasterCard masterCard = getMasterCardAuthenticatedUser();
        if (checkSubcategoryExistsById(masterCard, subcategoryId).isEmpty()) {
            throw new EntityNotFoundException("Not found subcategory id in master card");
        }
        if (masterCard.getGallery().size() >= limitPhoto) {
            throw new GalleryLimitExceededException(
                    "Exceeded the maximum limit for gallery photos. "
                            + "You can only upload up to " + limitPhoto + " photos.");
        }
        Photo newPhoto = photoRepository.save(
                new Photo(null, "-", masterCard, subcategoryId, false));
        String path = uploadDir + IMAGE_NAME + newPhoto.getId();
        uploadFileUtil.uploadFile(file, path + IMAGE_FORMAT);

        String fileName = IMAGE_NAME + newPhoto.getId() + IMAGE_FORMAT;
        newPhoto.setPhotoUrl(fileName);
        System.out.println("обрезка старт" + LocalDateTime.now());

        System.out.println("обрезка finish" + LocalDateTime.now());
        if (masterCard.getMainPhoto() == null) {
            executor.execute(() -> {
                createImageCopy(path);
                createMainImage(path);
            });
            masterCard.setMainPhoto(newPhoto);
            masterCardRepository.save(masterCard);
            newPhoto.setMain(true);
        } else {
            executor.execute(() -> {
                createImageCopy(path);
            });
        }
        Photo savePhoto = photoRepository.save(newPhoto);
        return new PhotoDto(savePhoto.getId(), fileName, savePhoto.isMain());
    }

    @Transactional
    @Override
    public void deletePhoto(Long id) {
        Photo photo = photoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found photo by id " + id));
        List<ServiceCard> serviceCards = serviceCardRepository.findAllByPhotoId(id);
        if (!serviceCards.isEmpty()) {
            for (ServiceCard serviceCard : serviceCards) {
                serviceCard.setPhoto(null);
                serviceCardRepository.save(serviceCard);
            }
        }
        if (photo.isMain()) {
            MasterCard masterCard = getMasterCardAuthenticatedUser();
            masterCard.setMainPhoto(null);
            masterCardRepository.save(masterCard);
        }
        String file = photo.getPhotoUrl();
        photoRepository.delete(photo);
        if (file.contains(":")) {
            file = file.replace(":", "\\");
        }
        deleteFile(file);
    }

    @Transactional
    @Override
    public void updateMainPhoto(Long id) {
        MasterCard masterCard = getMasterCardAuthenticatedUser();
        Photo photo = photoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found photo by id " + id));
        if (masterCard.getMainPhoto() != null) {
            Photo mainPhoto = masterCard.getMainPhoto();
            String name = mainPhoto.getPhotoUrl().substring(
                    0, mainPhoto.getPhotoUrl().length() - 4);
            deleteFile(name + "M" + IMAGE_FORMAT);
            deleteFile(name + "Mm" + IMAGE_FORMAT);
            mainPhoto.setMain(false);
            photoRepository.save(mainPhoto);
        }
        photo.setMain(true);
        masterCard.setMainPhoto(photoRepository.save(photo));
        masterCardRepository.save(masterCard);
        String newName = photo.getPhotoUrl().substring(0, photo.getPhotoUrl().length() - 4);

        executor.execute(() -> {
            createMainImage(uploadDir + newName);
        });
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PhotoDto> getPhotoByMasterCardAndSubcategory(
            Pageable pageable, Long subcategoryId, Long masterCardId) {
        if (masterCardId == null) {
            masterCardId = getMasterCardAuthenticatedUser().getId();
        }
        Page<Photo> photos = photoRepository.findAllByMasterCardIdAndSubcategoryId(
                pageable, masterCardId, subcategoryId);
        List<PhotoDto> photoDtos = photos.stream()
                .map(photoMapper::toDto)
                .toList();
        return new PageImpl<>(
                photoDtos,
                photos.getPageable(),
                photos.getTotalElements()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhotoDto> getRandomPhotoByMasterCard(Long masterCardId) {
        if (masterCardId == null) {
            masterCardId = getMasterCardAuthenticatedUser().getId();
        }
        return photoRepository.findPhotosByMasterCardId(masterCardId).stream()
                .limit(4)
                .map(photoMapper::toDto)
                .toList();
    }

    private MasterCard getMasterCardAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return masterCardRepository.findByUserUuid(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not fount master card by user email: "
                                + authentication.getName()
                ));
    }

    private void createImageCopy(String path) {
        BufferedImage image = ImageUtil.readImage(path + IMAGE_FORMAT);

        System.out.println("обрезка старт 1" + LocalDateTime.now());
        ImageUtil.resizeImage(
                image,
                path + "G" + IMAGE_FORMAT,
                498, 380
        );
        System.out.println("обрезка finish 1 " + LocalDateTime.now());
        System.out.println("обрезка старт 2" + LocalDateTime.now());
        ImageUtil.resizeImage(
                image,
                path + "C" + IMAGE_FORMAT,
                85,
                85
        );
    }

    private void createMainImage(String path) {
        BufferedImage image = ImageUtil.readImage(path + IMAGE_FORMAT);
        ImageUtil.resizeImage(
                image,
                path + "M" + IMAGE_FORMAT,
                758, 436
        );
        ImageUtil.resizeImage(
                image,
                path + "Mm" + IMAGE_FORMAT,
                270,
                200
        );
    }

    private Optional<Subcategory> checkSubcategoryExistsById(
            MasterCard masterCard, Long subcategoryId) {
        return masterCard.getSubcategories().stream()
                .filter(subcategory -> subcategory.getId().equals(subcategoryId))
                .findFirst();
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
