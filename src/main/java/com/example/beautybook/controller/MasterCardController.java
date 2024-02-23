package com.example.beautybook.controller;

import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.dto.mastercard.MasterCardDto;
import com.example.beautybook.dto.mastercard.MasterCardResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardUpdateDto;
import com.example.beautybook.service.MasterCardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MasterCardController {
    private final MasterCardService masterCardService;

    @PostMapping("/new-master")
    MasterCardDto createMasterCard() {
        return masterCardService.createNewMasterCard();
    }

    @GetMapping("/masterSortByRating")
    Page<MasterCardResponseDto> getTop20MasterCard(
            @RequestParam(defaultValue = "0")
            int pageNumber,
            @RequestParam(defaultValue = "20")
            int pageSize
    ) {
        return masterCardService.getTop20MasterCard(pageNumber, pageSize);
    }

    @GetMapping("/master/{id}")
    MasterCardDto getMasterCardById(@PathVariable Long id) {
        return masterCardService.getMasterCardById(id);
    }

    @GetMapping("/master")
    MasterCardDto getUserMasterCard() {
        return masterCardService.getUserMasterCard();
    }

    @PutMapping("/master")
    MasterCardDto updateMasterCart(@RequestBody
                                   @Valid
                                   MasterCardUpdateDto masterCardUpdateDto) {
        return masterCardService.updateMasterCard(masterCardUpdateDto);
    }

    @GetMapping("/master/search")
    Page<MasterCardResponseDto> searchMasterCard(
            @RequestBody SearchParam searchParam
    ) {
        return masterCardService.searchMasterCard(searchParam);
    }
}
