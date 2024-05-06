package com.example.beautybook.service.impl;

import com.example.beautybook.dto.category.SubcategoryResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardCreateDto;
import com.example.beautybook.dto.mastercard.MasterCardCreateResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardDto;
import com.example.beautybook.dto.mastercard.MasterCardResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardUpdateDto;
import com.example.beautybook.dto.mastercard.MasterCardUpdateResponseDto;
import com.example.beautybook.dto.photo.PhotoDto;
import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.mapper.MasterCardMapper;
import com.example.beautybook.mapper.SubcategoryMapper;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.model.Subcategory;
import com.example.beautybook.model.User;
import com.example.beautybook.repository.categoty.SubcategoryRepository;
import com.example.beautybook.repository.mastercard.MasterCardRepository;
import com.example.beautybook.repository.mastercard.ReviewRepository;
import com.example.beautybook.repository.user.SpecificationBuilder;
import com.example.beautybook.repository.user.UserRepository;
import com.example.beautybook.service.MasterCardService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MasterCardServiceImpl implements MasterCardService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final MasterCardRepository masterCardRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final MasterCardMapper masterCardMapper;
    private final SubcategoryMapper subcategoryMapper;
    private final SpecificationBuilder<MasterCard> masterCardSpecificationBuilder;
    private final Random random;

    @Override
    @Transactional
    public MasterCardCreateResponseDto createNewMasterCard(MasterCardCreateDto dto) {
        User user = getAuthenticatedUser();
        MasterCard masterCard = masterCardMapper.toModel(dto);
        masterCard.setUser(user);
        return masterCardMapper.toCreateResponseDto(masterCardRepository.save(masterCard));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MasterCardResponseDto> getTop20MasterCard(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "rating"));
        Page<MasterCard> masterCards = masterCardRepository.findAllByOrderByRatingDesc(pageable);
        List<MasterCardResponseDto> dtos = masterCards.getContent().stream()
                .map(masterCardMapper::toResponseDto)
                .toList();
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            isFavoriteMasterCard(dtos, getAuthenticatedUser().getFavoriteMasterCards());
        }
        return new PageImpl<>(
                dtos,
                masterCards.getPageable(),
                masterCards.getTotalElements()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public MasterCardUpdateResponseDto getUserMasterCard() {
        MasterCard masterCard = getAuthenticatedUserMasterCard();
        return masterCardMapper.toUpdateResponseDto(masterCard);
    }

    @Override
    @Transactional
    public MasterCardDto updateMasterCard(MasterCardUpdateDto masterCardUpdateDto) {
        MasterCard masterCard = getAuthenticatedUserMasterCard();
        masterCardMapper.updateModelForDto(masterCard, masterCardUpdateDto);
        return masterCardMapper.toDto(masterCardRepository.save(masterCard));
    }

    @Override
    @Transactional(readOnly = true)
    public MasterCardDto getMasterCardById(Long id) {
        MasterCard masterCard = masterCardRepository.findMasterCardByIdAndIsHiddenIsFalse(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found Master card by id: " + id)

                );
        MasterCardDto dto = masterCardMapper.toDto(masterCard);
        dto.setStatistics(reviewRepository.getCountsByGrade(id));
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MasterCardResponseDto> searchMasterCard(SearchParam param, Pageable pageable) {
        Page<MasterCard> masterCards = masterCardRepository.findAll(
                masterCardSpecificationBuilder.build(param), pageable);
        List<MasterCardResponseDto> dtoList = masterCards.stream()
                .map(masterCardMapper::toResponseDto)
                .toList();
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
            isFavoriteMasterCard(dtoList, getAuthenticatedUser().getFavoriteMasterCards());
        }
        return new PageImpl<>(
                dtoList, masterCards.getPageable(),
                masterCards.getTotalElements()
        );
    }

    @Override
    @Transactional
    public List<SubcategoryResponseDto> addSubcategory(Long id) {
        Subcategory subcategory = subcategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found subcategory by id " + id)
                );
        MasterCard masterCard = getAuthenticatedUserMasterCard();
        masterCard.getSubcategories().add(subcategory);
        masterCardRepository.save(masterCard);
        return masterCard.getSubcategories().stream()
                .map(subcategoryMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public List<SubcategoryResponseDto> deleteSubcategoryFromList(Long id) {
        MasterCard masterCard = getAuthenticatedUserMasterCard();
        Subcategory sub = masterCard.getSubcategories().stream()
                .filter(subcategory -> subcategory.getId().equals(id))
                .findFirst().orElseThrow(
                        () -> new EntityNotFoundException(
                                "Not found subcategory by " + id + "from list")
                );
        boolean isPresentServiceCard = masterCard.getServiceCards().stream()
                .anyMatch(serviceCard -> serviceCard.getSubcategory().equals(sub));
        if (isPresentServiceCard) {
            throw new RuntimeException("");
        }

        boolean isPresentPhoto = masterCard.getGallery().stream()
                .anyMatch(photo -> photo.getSubcategoryId().equals(id));
        if (isPresentPhoto) {
            throw new RuntimeException("");
        }

        masterCard.getSubcategories().remove(sub);
        return masterCardRepository.save(masterCard).getSubcategories().stream()
                .map(subcategoryMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteMasterCard() {
        MasterCard masterCard = getAuthenticatedUserMasterCard();
        masterCard.setDeleted(true);
        masterCard.setUser(null);
        masterCardRepository.save(masterCard);
    }

    @Override
    @Transactional
    public void hideMasterCard() {
        MasterCard masterCard = getAuthenticatedUserMasterCard();
        masterCard.setHidden(true);
        masterCardRepository.save(masterCard);
    }

    @Override
    @Transactional
    public void unhideMasterCard() {
        MasterCard masterCard = getAuthenticatedUserMasterCard();
        masterCard.setHidden(false);
        masterCardRepository.save(masterCard);
    }

    @Override
    @Transactional
    public void addFavoriteMasterCard(Long masterCardId) {
        User user = getAuthenticatedUser();
        user.getFavoriteMasterCards().add(new MasterCard(masterCardId));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteFavoriteMasterCard(Long masterCardId) {
        User user = getAuthenticatedUser();
        user.getFavoriteMasterCards().stream()
                .filter(masterCard -> masterCard.getId().equals(masterCardId))
                .findFirst()
                .ifPresent(masterCard -> user.getFavoriteMasterCards().remove(masterCard));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public Page<MasterCardResponseDto> getFavoriteMasterCard(Pageable pageable) {
        User user = getAuthenticatedUser();
        Page<MasterCardResponseDto> responseDtos =
                userRepository.findFavoriteMasterCardsByUser(user, pageable)
                        .map(masterCardMapper::toResponseDto);
        responseDtos.forEach(dto -> dto.setFavorite(true));
        return responseDtos;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUuid(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Not fount user by email: "
                        + authentication.getName()));
    }

    private MasterCard getAuthenticatedUserMasterCard() {
        Optional<MasterCard> byUserUuid = masterCardRepository.findByUserUuid(
                SecurityContextHolder.getContext().getAuthentication().getName());
        return byUserUuid.orElseThrow(() -> new EntityNotFoundException(
                "Not found master card by Authentication user"
        ));
    }

    private List<PhotoDto> getRandomPhotoDto(List<PhotoDto> photos, int size) {
        if (photos.size() <= size) {
            return photos;
        }
        List<PhotoDto> randomList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            randomList.add(photos.get(random.nextInt(0, photos.size() - 1)));
        }
        return randomList;//TODO
    }

    private void isFavoriteMasterCard(List<MasterCardResponseDto> list, Set<MasterCard> favorite) {
        List<Long> id = new ArrayList<>();
        for (MasterCard m : favorite) {
            id.add(m.getId());
        }
        List<Long> favoriteIds = favorite.stream().map(MasterCard::getId).toList();
        list.forEach(dto -> {
            if (favoriteIds.contains(dto.getId())) {
                dto.setFavorite(true);
                }
        });
    }
}
