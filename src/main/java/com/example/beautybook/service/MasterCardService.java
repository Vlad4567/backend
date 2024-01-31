package com.example.beautybook.service;

import com.example.beautybook.dto.mastercard.MasterCardDto;

public interface MasterCardService {
    MasterCardDto createNewMasterCard(Long userId);
}
