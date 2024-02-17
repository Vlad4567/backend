package com.example.beautybook.service;

import com.example.beautybook.dto.SearchParam;
import com.example.beautybook.dto.mastercard.MasterCardDto;
import com.example.beautybook.dto.mastercard.MasterCardResponseDto;
import com.example.beautybook.dto.mastercard.MasterCardUpdateDto;
import org.springframework.data.domain.Page;

public interface MasterCardService {
    MasterCardDto createNewMasterCard();

    Page<MasterCardResponseDto> getTop20MasterCard(int pageNumber, int pageSize);

    MasterCardDto getUserMasterCard();

    MasterCardDto updateMasterCard(MasterCardUpdateDto masterCardUpdateDto);

    MasterCardDto getMasterCardById(Long id);

    Page<MasterCardResponseDto> searchMasterCard(SearchParam param);
}
