package com.example.beautybook.controller;

import com.example.beautybook.dto.mastercard.MasterCardDto;
import com.example.beautybook.service.MasterCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MasterCardController {
    private final MasterCardService masterCardService;

    @PostMapping("/master")
    MasterCardDto createMasterCard() {
        return masterCardService.createNewMasterCard();
    }
}
