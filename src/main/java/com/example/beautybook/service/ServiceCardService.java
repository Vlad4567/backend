package com.example.beautybook.service;

import com.example.beautybook.dto.servicecard.ServiceCardCreateDto;
import com.example.beautybook.dto.servicecard.ServiceCardDto;
import java.util.List;

public interface ServiceCardService {
    List<ServiceCardDto> getAllByMasterCardId(Long masterCardId);

    ServiceCardDto createServiceCard(ServiceCardCreateDto serviceCardCreateDto);
}
