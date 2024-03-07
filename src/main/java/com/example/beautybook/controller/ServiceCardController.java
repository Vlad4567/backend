package com.example.beautybook.controller;

import com.example.beautybook.dto.PageableDto;
import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.dto.servicecard.ServiceCardCreateDto;
import com.example.beautybook.dto.servicecard.ServiceCardDto;
import com.example.beautybook.dto.servicecard.ServiceCardSearchDto;
import com.example.beautybook.service.ServiceCardService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(
        origins = "*",
        allowedHeaders = {"Content-Type", "Authorization"})
@RequiredArgsConstructor
@RestController
public class ServiceCardController {
    private final ServiceCardService serviceCardService;

    @GetMapping("/master/{masterId}/servicecard")
    public List<ServiceCardDto> getServiceCardsByMasterIdAndSubcategoryId(
            @PathVariable
            Long masterId,
            @NotNull
            Long subcategoryId) {
        return serviceCardService.getAllByMasterCardIdAndSubcategoryId(masterId, subcategoryId);
    }

    @PostMapping("/servicecard")
    public ServiceCardDto createServiceCard(
            @RequestBody
            @Valid
            ServiceCardCreateDto serviceCardCreateDto) {
        return serviceCardService.createServiceCard(serviceCardCreateDto);
    }

    @GetMapping("/servicecard/search")
    public Page<ServiceCardSearchDto> search(@Valid SearchParam param, PageableDto pageableDto) {
        String property;
        if (pageableDto.property().equalsIgnoreCase("rating")) {
            property = "masterCard.rating";
        } else {
            property = pageableDto.property();
        }
        Pageable pageable = PageRequest.of(
                pageableDto.page(),
                pageableDto.size(),
                Sort.by(pageableDto.direction(), property)
        );
        return serviceCardService.search(param, pageable);
    }
}
