package com.example.beautybook.controller;

import com.example.beautybook.dto.PageableDto;
import com.example.beautybook.dto.category.SubcategoryResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardCreateDto;
import com.example.beautybook.dto.mastercard.MasterCardCreateResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardDto;
import com.example.beautybook.dto.mastercard.MasterCardResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardUpdateDto;
import com.example.beautybook.dto.mastercard.MasterCardUpdateResponseDto;
import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.service.MasterCardService;
import com.example.beautybook.util.impl.PageableUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(
        origins = "*",
        allowedHeaders = {"Content-Type", "Authorization"})
@RequiredArgsConstructor
@RestController
public class MasterCardController {
    private final MasterCardService masterCardService;

    @PostMapping("/new-master")
    MasterCardCreateResponseDto createMasterCard(
            @RequestBody
            @Valid
            MasterCardCreateDto dto
    ) {
        return masterCardService.createNewMasterCard(dto);
    }

    @GetMapping("/masterSortByRating")
    Page<MasterCardResponseDto> getTop20MasterCard(
            HttpServletRequest request,
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
    MasterCardUpdateResponseDto getUserMasterCard() {
        return masterCardService.getUserMasterCard();
    }

    @PutMapping("/master")
    MasterCardDto updateMasterCart(@RequestBody
                                   @Valid
                                   MasterCardUpdateDto masterCardUpdateDto) {
        return masterCardService.updateMasterCard(masterCardUpdateDto);
    }

    @GetMapping("/master/search")
    Page<MasterCardResponseDto> searchMasterCard(PageableDto pageableDto, SearchParam searchParam) {
        return masterCardService.searchMasterCard(
                searchParam, PageableUtil.createPageable(pageableDto));
    }

    @PostMapping("/master/subcategory")
    List<SubcategoryResponseDto> addSubcategory(
            @NotNull(message = "Сan not be empty")
            Long id
    ) {
        return masterCardService.addSubcategory(id);
    }

    @DeleteMapping("/master/subcategory")
    List<SubcategoryResponseDto> deleteSubcategoryFromList(
            @NotNull(message = "Сan not be empty")
            Long id
    ) {
        return masterCardService.deleteSubcategoryFromList(id);
    }

    @DeleteMapping("/master")
    void deleteMasterCard() {
        masterCardService.deleteMasterCard();
    }

    @PutMapping("/master/hide")
    void hideMasterCard() {
        masterCardService.hideMasterCard();
    }

    @PutMapping("/master/unhide")
    void unhideMasterCard() {
        masterCardService.unhideMasterCard();
    }
}
