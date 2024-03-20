package com.example.beautybook.controller;

import com.example.beautybook.dto.PageableDto;
import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.dto.servicecard.ServiceCardCreateDto;
import com.example.beautybook.dto.servicecard.ServiceCardDto;
import com.example.beautybook.dto.servicecard.ServiceCardSearchDto;
import com.example.beautybook.service.ServiceCardService;
import com.example.beautybook.util.impl.PageableUtil;
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
        if (pageableDto.getProperty() != null && pageableDto.getProperty().equals("rating")) {
            pageableDto.setProperty("masterCard.rating");
        }
        return serviceCardService.search(param, PageableUtil.createPageable(pageableDto));
    }

    @DeleteMapping("/serviceCard/{id}")
    void deleteServiceCard(@PathVariable Long id) {
        serviceCardService.deleteServiceCard(id);
    }

    @PutMapping("/serviceCard/{id}")
    ServiceCardDto updateServiceCard(
            @NotNull
            @PathVariable
            Long id,
            @Valid
            @NotNull
            @RequestBody
            ServiceCardCreateDto dto
    ) {
        return serviceCardService.updateServiceCard(id, dto);
    }
}
