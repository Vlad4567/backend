package com.example.beautybook.service.impl;

import com.example.beautybook.dto.mastercard.MasterCardDto;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.mapper.MasterCardMapper;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.model.User;
import com.example.beautybook.repository.MasterCardRepository;
import com.example.beautybook.repository.UserRepository;
import com.example.beautybook.service.MasterCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MasterCardServiceImpl implements MasterCardService {
    private final UserRepository userRepository;
    private final MasterCardRepository masterCardRepository;
    private final MasterCardMapper masterCardMapper;

    @Override
    public MasterCardDto createNewMasterCard() {
        User user = getAuthenticatedUser();
        MasterCard masterCard = new MasterCard();
        masterCard.setUser(user);
        return masterCardMapper.toDto(masterCardRepository.save(masterCard));

    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("Not fount user by email: "
                        + authentication.getName()));
    }
}
