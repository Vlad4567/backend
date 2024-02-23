package com.example.beautybook.service;

import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.dto.servicecard.ServiceCardCreateDto;
import com.example.beautybook.dto.servicecard.ServiceCardDto;
import com.example.beautybook.dto.servicecard.ServiceCardSearchDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ServiceCardService {
    List<ServiceCardDto> getAllByMasterCardId(Long masterCardId);

    ServiceCardDto createServiceCard(ServiceCardCreateDto serviceCardCreateDto);

    Page<ServiceCardSearchDto> search(SearchParam param);
}
