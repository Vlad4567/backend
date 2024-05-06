package com.example.beautybook.service;

import com.example.beautybook.dto.category.SubcategoryResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardCreateDto;
import com.example.beautybook.dto.mastercard.MasterCardCreateResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardDto;
import com.example.beautybook.dto.mastercard.MasterCardResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardUpdateDto;
import com.example.beautybook.dto.mastercard.MasterCardUpdateResponseDto;
import com.example.beautybook.dto.search.SearchParam;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MasterCardService {
    MasterCardCreateResponseDto createNewMasterCard(MasterCardCreateDto dto);

    Page<MasterCardResponseDto> getTop20MasterCard(int pageNumber, int pageSize);

    MasterCardUpdateResponseDto getUserMasterCard();

    MasterCardDto updateMasterCard(MasterCardUpdateDto masterCardUpdateDto);

    MasterCardDto getMasterCardById(Long id);

    Page<MasterCardResponseDto> searchMasterCard(SearchParam param, Pageable pageable);

    List<SubcategoryResponseDto> addSubcategory(Long id);

    List<SubcategoryResponseDto> deleteSubcategoryFromList(Long id);

    void deleteMasterCard();

    void hideMasterCard();

    void unhideMasterCard();

    void addFavoriteMasterCard(Long masterCardId);

    void deleteFavoriteMasterCard(Long masterCardId);

    Page<MasterCardResponseDto> getFavoriteMasterCard(Pageable pageable);
}
