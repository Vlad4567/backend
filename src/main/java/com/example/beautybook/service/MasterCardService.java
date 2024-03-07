package com.example.beautybook.service;

import com.example.beautybook.dto.category.SubcategoryResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardCreateDto;
import com.example.beautybook.dto.mastercard.MasterCardDto;
import com.example.beautybook.dto.mastercard.MasterCardResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardUpdateDto;
import com.example.beautybook.dto.search.SearchParam;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MasterCardService {
    MasterCardDto createNewMasterCard(MasterCardCreateDto dto, MultipartFile file);

    Page<MasterCardResponseDto> getTop20MasterCard(int pageNumber, int pageSize);

    MasterCardDto getUserMasterCard();

    MasterCardDto updateMasterCard(MasterCardUpdateDto masterCardUpdateDto);

    MasterCardDto getMasterCardById(Long id);

    Page<MasterCardResponseDto> searchMasterCard(SearchParam param, Pageable pageable);

    List<SubcategoryResponseDto> addSubcategory(Long id);
}
