package com.example.beautybook.service.impl;

import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.dto.servicecard.ServiceCardCreateDto;
import com.example.beautybook.dto.servicecard.ServiceCardDto;
import com.example.beautybook.dto.servicecard.ServiceCardSearchDto;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.mapper.ServiceCardMapper;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.model.ServiceCard;
import com.example.beautybook.repository.mastercard.MasterCardRepository;
import com.example.beautybook.repository.servicecard.ServiceCardRepository;
import com.example.beautybook.repository.user.SpecificationBuilder;
import com.example.beautybook.service.ServiceCardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceCardServiceImpl implements ServiceCardService {
    private final ServiceCardRepository serviceCardRepository;
    private final ServiceCardMapper serviceCardMapper;
    private final MasterCardRepository masterCardRepository;
    private final SpecificationBuilder<ServiceCard> specificationBuilder;

    @Override
    public List<ServiceCardDto> getAllByMasterCardId(Long masterCardId) {
        return serviceCardRepository.findAllByMasterCardId(masterCardId).stream()
                .map(serviceCardMapper::toDto)
                .toList();
    }

    @Override
    public ServiceCardDto createServiceCard(ServiceCardCreateDto serviceCardCreateDto) {
        ServiceCard serviceCard = serviceCardMapper.toModel(serviceCardCreateDto);
        serviceCard.setMasterCard(getMasterCardAuthenticatedUser());
        return serviceCardMapper.toDto(serviceCardRepository.save(serviceCard));
    }

    @Override
    public Page<ServiceCardSearchDto> search(SearchParam param, Pageable pageable) {
        Page<ServiceCard> serviceCards =
                serviceCardRepository.findAll(
                        specificationBuilder.build(param), pageable);
        List<ServiceCardSearchDto> listDto = serviceCards.getContent().stream()
                .map(serviceCardMapper::toSearchDto)
                .toList();
        return new PageImpl<>(
                listDto,
                serviceCards.getPageable(),
                serviceCards.getTotalElements()
        );
    }

    @Override
    public List<ServiceCardDto> getAllByMasterCardIdAndSubcategoryId(
            Long masterId, Long subcategoryId) {
        List<ServiceCard> serviceCards = serviceCardRepository
                .findAllByMasterCardIdAndSubcategoryId(masterId, subcategoryId);
        return serviceCards.stream()
                .map(serviceCardMapper::toDto)
                .toList();
    }

    private MasterCard getMasterCardAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return masterCardRepository.findByUserEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Not fount master card by user email: "
                                + authentication.getName()
                ));
    }
}
