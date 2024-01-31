package com.example.beautybook.controller;

import com.example.beautybook.dto.mastercard.MasterCardDto;
import com.example.beautybook.service.MasterCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MasterCardController {
    private final MasterCardService masterCardService;

    @PutMapping("/master/{id}")
    MasterCardDto createMasterCard(@PathVariable Long id) {
        return masterCardService.createNewMasterCard(id);
    }
}
