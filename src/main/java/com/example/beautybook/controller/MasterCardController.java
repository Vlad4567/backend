package com.example.beautybook.controller;

import com.example.beautybook.dto.PageableDto;
import com.example.beautybook.dto.category.SubcategoryResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardCreateDto;
import com.example.beautybook.dto.mastercard.MasterCardDto;
import com.example.beautybook.dto.mastercard.MasterCardResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardUpdateDto;
import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.service.MasterCardService;
import com.example.beautybook.validation.ImageFile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(
        origins = "*",
        allowedHeaders = {"Content-Type", "Authorization"})
@RequiredArgsConstructor
@RestController
public class MasterCardController {
    private final MasterCardService masterCardService;

    @PostMapping("/new-master")
    MasterCardDto createMasterCard(
            @RequestBody
            @Valid MasterCardCreateDto dto,
            @RequestParam("file")
            @Valid
            @ImageFile
            MultipartFile file
    ) {
        return masterCardService.createNewMasterCard(dto, file);
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
    MasterCardDto getMasterCardById(@PathVariable Long id, HttpServletRequest request) {
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
    Page<MasterCardResponseDto> searchMasterCard(PageableDto pageableDto, SearchParam searchParam) {
        Pageable pageable = PageRequest.of(
                pageableDto.page(),
                pageableDto.size(),
                Sort.by(pageableDto.direction(), pageableDto.property())
        );
        return masterCardService.searchMasterCard(searchParam, pageable);
    }

    @PostMapping("/master/subcategory")
    List<SubcategoryResponseDto> addSubcategory(@NotNull Long id) {
        return masterCardService.addSubcategory(id);
    }
}
