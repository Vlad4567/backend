package com.example.beautybook.service.impl;

import com.example.beautybook.dto.Statistics;
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
import com.example.beautybook.repository.user.SpecificationBuilder;
import com.example.beautybook.repository.user.UserRepository;
import com.example.beautybook.service.MasterCardService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MasterCardServiceImpl implements MasterCardService {
    private final UserRepository userRepository;
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
    public Page<MasterCardResponseDto> getTop20MasterCard(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(
                pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "rating"));
        Page<MasterCard> masterCards = masterCardRepository.findAllByOrderByRatingDesc(pageable);
        List<MasterCardResponseDto> dtos = masterCards.getContent().stream()
                .map(masterCardMapper::toResponseDto)
                .toList();
        return new PageImpl<>(
                dtos,
                masterCards.getPageable(),
                masterCards.getTotalElements()
        );
    }

    @Override
    public MasterCardUpdateResponseDto getUserMasterCard() {
        MasterCard masterCard = getAuthenticatedUserMasterCard();
        return masterCardMapper.toUpdateResponseDto(masterCard);
    }

    @Override
    public MasterCardDto updateMasterCard(MasterCardUpdateDto masterCardUpdateDto) {
        MasterCard masterCard = getAuthenticatedUserMasterCard();
        masterCardMapper.updateModelForDto(masterCard, masterCardUpdateDto);
        return masterCardMapper.toDto(masterCardRepository.save(masterCard));
    }

    @Override
    public MasterCardDto getMasterCardById(Long id) {
        MasterCard masterCard = masterCardRepository.findMasterCardByIdAndIsHiddenFalse(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found Master card by id: " + id)

                );
        MasterCardDto dto = masterCardMapper.toDto(masterCard);
        dto.setStatistics(new Statistics(masterCardRepository.getCountsByGrade(id)));
        dto.setGallery(getRandomPhotoDto(dto.getGallery(), 4));
        return dto;
    }

    @Override
    public Page<MasterCardResponseDto> searchMasterCard(SearchParam param, Pageable pageable) {
        Page<MasterCard> masterCards = masterCardRepository.findAll(
                masterCardSpecificationBuilder.build(param), pageable);
        List<MasterCardResponseDto> dtoList = masterCards.stream()
                .map(masterCardMapper::toResponseDto)
                .toList();
        return new PageImpl<>(
                dtoList, masterCards.getPageable(),
                masterCards.getTotalElements()
        );
    }

    @Transactional
    @Override
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
    public void deleteMasterCard() {
        MasterCard masterCard = getAuthenticatedUserMasterCard();
        masterCard.setDeleted(true);
        masterCard.setUser(null);
        masterCardRepository.save(masterCard);
    }

    @Override
    public void hideMasterCard() {
        MasterCard masterCard = getAuthenticatedUserMasterCard();
        masterCard.setHidden(true);
        masterCardRepository.save(masterCard);
    }

    @Override
    public void unhideMasterCard() {
        MasterCard masterCard = getAuthenticatedUserMasterCard();
        masterCard.setHidden(false);
        masterCardRepository.save(masterCard);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Not fount user by email: "
                        + authentication.getName()));
    }

    private MasterCard getAuthenticatedUserMasterCard() {
        return masterCardRepository.findByUserEmail(
                SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(() -> new EntityNotFoundException(
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
        return randomList;
    }
}
