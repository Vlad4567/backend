package com.example.beautybook.service.impl;

import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.dto.servicecard.ServiceCardCreateDto;
import com.example.beautybook.dto.servicecard.ServiceCardDto;
import com.example.beautybook.dto.servicecard.ServiceCardSearchDto;
import com.example.beautybook.exceptions.AccessDeniedException;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.mapper.ServiceCardMapper;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.model.Photo;
import com.example.beautybook.model.ServiceCard;
import com.example.beautybook.repository.mastercard.MasterCardRepository;
import com.example.beautybook.repository.photo.PhotoRepository;
import com.example.beautybook.repository.servicecard.ServiceCardRepository;
import com.example.beautybook.repository.user.SpecificationBuilder;
import com.example.beautybook.service.ServiceCardService;
import com.example.beautybook.util.impl.ImageUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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

@Service
@RequiredArgsConstructor
public class ServiceCardServiceImpl implements ServiceCardService {
    private static final String IMAGE_FORMAT = ".jpg";
    private static final int WIDTH_PROFILE_PHOTO = 269;
    private static final int HEIGHT_PROFILE_PHOTO = 200;
    private final ServiceCardRepository serviceCardRepository;
    private final ServiceCardMapper serviceCardMapper;
    private final MasterCardRepository masterCardRepository;
    private final PhotoRepository photoRepository;
    private final SpecificationBuilder<ServiceCard> specificationBuilder;
    private final ScheduledExecutorService executor;
    @Value("${uploud.dir}")
    private String uploadDir;

    @Override
    @Transactional(readOnly = true)
    public List<ServiceCardDto> getAllByMasterCardId(Long masterCardId) {
        return serviceCardRepository.findAllByMasterCardId(masterCardId).stream()
                .map(serviceCardMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ServiceCardDto createServiceCard(ServiceCardCreateDto serviceCardCreateDto) {
        ServiceCard serviceCard = serviceCardMapper.toModel(serviceCardCreateDto);
        MasterCard masterCard = getMasterCardAuthenticatedUser();
        serviceCard.setMasterCard(masterCard);
        if (serviceCard.getPhoto() != null) {
            Photo photo = photoRepository.findById(serviceCard.getPhoto().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Not found photo by id"));
            String path = uploadDir + photo.getPhotoUrl();
            String newPath = ImageUtil.updatePath(path, "S");
            if (!new File(newPath).exists()) {
                executor.execute(() -> ImageUtil.resizeImage(
                                ImageUtil.readImage(path),
                                newPath,
                                WIDTH_PROFILE_PHOTO,
                                HEIGHT_PROFILE_PHOTO
                        )
                );
            }
        }
        return serviceCardMapper.toDto(serviceCardRepository.save(serviceCard));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ServiceCardSearchDto> search(SearchParam param, Pageable pageable) {
        Page<ServiceCard> serviceCards =
                serviceCardRepository.findAll(
                        specificationBuilder.build(param), pageable);
        List<ServiceCardSearchDto> listDto = serviceCards.getContent().stream()
                .map(serviceCardMapper::toSearchDto)
                .toList();
        return new PageImpl<>(
                listDto,
                serviceCards.getPageable(),
                serviceCards.getTotalElements()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceCardDto> getAllByMasterCardIdAndSubcategoryId(
            Long masterId, Long subcategoryId) {
        List<ServiceCard> serviceCards = serviceCardRepository
                .findAllByMasterCardIdAndSubcategoryId(masterId, subcategoryId);
        return serviceCards.stream()
                .map(serviceCardMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteServiceCard(Long id) {
        Long masterCardId = getMasterCardAuthenticatedUser().getId();
        ServiceCard serviceCard = serviceCardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found service card by id " + id));
        if (!serviceCard.getMasterCard().getId().equals(masterCardId)) {
            throw new AccessDeniedException("The authorized user does not have "
                    + "a service card by ID " + id);
        }
        serviceCard.setDeleted(true);
        serviceCardRepository.save(serviceCard);
    }

    @Override
    @Transactional
    public ServiceCardDto updateServiceCard(Long id, ServiceCardCreateDto dto) {
        MasterCard masterCard = getMasterCardAuthenticatedUser();
        Long masterCardId = masterCard.getId();
        ServiceCard serviceCard = serviceCardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Not found service card by id " + id));
        if (!serviceCard.getMasterCard().getId().equals(masterCardId)) {
            throw new AccessDeniedException("The authorized user does not have "
                    + "a service card by ID " + id);
        }

        serviceCardMapper.updateServiceCardForDto(serviceCard, dto);

        if (dto.getPhotoId() != null) {
            Photo newPhoto = photoRepository.findById(dto.getPhotoId()).orElseThrow(
                    () -> new EntityNotFoundException("Not found photo by id"));

            if (serviceCard.getPhoto() == null) {
                String path = uploadDir + newPhoto.getPhotoUrl();
                createResizedImage(path, ImageUtil.updatePath(path, "S"));
            } else {
                Photo photo = photoRepository.findById(serviceCard.getPhoto().getId()).orElseThrow(
                        () -> new EntityNotFoundException("Not found photo by id"));
                updateServiceCardPhoto(photo, newPhoto, masterCard);
            }

        } else {
            serviceCard.setPhoto(null);
        }
        return serviceCardMapper.toDto(serviceCardRepository.save(serviceCard));
    }

    private MasterCard getMasterCardAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return masterCardRepository.findByUserUuid(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not fount master card by user email: "
                                + authentication.getName()
                ));
    }

    private void deleteFile(String path) {
        Path filePath = Path.of(path);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting the infected file: " + path, e);
        }
    }

    private void updateServiceCardPhoto(Photo photo, Photo newPhoto, MasterCard masterCard) {
        String path = ImageUtil.updatePath(uploadDir + photo.getPhotoUrl(), "S");
        String newPath = ImageUtil.updatePath(uploadDir + newPhoto.getPhotoUrl(), "S");
        masterCard.getServiceCards().stream()
                .filter(s -> s.getPhoto() == photo)
                .findFirst()
                .ifPresent(serviceCard -> deleteFile(path));
        createResizedImage(uploadDir + photo.getPhotoUrl(), newPath);
    }

    private void createResizedImage(String path, String newPath) {
        if (!new File(newPath).exists()) {
            executor.execute(() -> {
                ImageUtil.resizeImage(
                        ImageUtil.readImage(path),
                        newPath,
                        WIDTH_PROFILE_PHOTO,
                        HEIGHT_PROFILE_PHOTO
                );
            });
        }
    }
}
