package com.example.beautybook.service.impl;

import com.example.beautybook.dto.SearchParam;
import com.example.beautybook.dto.mastercard.MasterCardDto;
import com.example.beautybook.dto.mastercard.MasterCardResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardUpdateDto;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.mapper.MasterCardMapper;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.model.User;
import com.example.beautybook.repository.mastercard.MasterCardRepository;
import com.example.beautybook.repository.user.SpecificationBuilder;
import com.example.beautybook.repository.user.UserRepository;
import com.example.beautybook.service.MasterCardService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MasterCardServiceImpl implements MasterCardService {
    private final UserRepository userRepository;
    private final MasterCardRepository masterCardRepository;
    private final MasterCardMapper masterCardMapper;
    private final SpecificationBuilder<MasterCard> masterCardSpecificationBuilder;

    @Override
    @Transactional
    public MasterCardDto createNewMasterCard() {
        User user = getAuthenticatedUser();
        MasterCard masterCard = new MasterCard();
        masterCard.setUser(user);
        return masterCardMapper.toDto(masterCardRepository.save(masterCard));

    }

    @Override
    public Page<MasterCardResponseDto> getTop20MasterCard(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
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
    public MasterCardDto getUserMasterCard() {
        MasterCard masterCard = getAuthenticatedUserMasterCard();
        return masterCardMapper.toDto(masterCard);
    }

    @Override
    public MasterCardDto updateMasterCard(MasterCardUpdateDto masterCardUpdateDto) {
        MasterCard masterCard = getAuthenticatedUserMasterCard();
        masterCardMapper.updateModelForDto(masterCard, masterCardUpdateDto);
        return masterCardMapper.toDto(masterCardRepository.save(masterCard));
    }

    @Override
    public MasterCardDto getMasterCardById(Long id) {
        MasterCard masterCard = masterCardRepository.findMasterCardById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found Master card by id: " + id)
                );
        return masterCardMapper.toDto(masterCard);
    }

    @Override
    public Page<MasterCardResponseDto> searchMasterCard(SearchParam param) {

        List<MasterCard> list = masterCardRepository.findAll(
                masterCardSpecificationBuilder.build(param));
        return null;

    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Not fount user by email: "
                        + authentication.getName()));
    }

    private MasterCard getAuthenticatedUserMasterCard() {
        return masterCardRepository.findByUserEmail(getAuthenticatedUser().getEmail())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not found master card by Authentication user"
                ));
    }
}
