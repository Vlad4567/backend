package com.example.beautybook.controller;

import com.example.beautybook.dto.servicecard.ServiceCardCreateDto;
import com.example.beautybook.dto.servicecard.ServiceCardDto;
import com.example.beautybook.service.ServiceCardService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ServiceCardController {
    private final ServiceCardService serviceCardService;

    @GetMapping("/mastercard/{id}/servicecard")
    public List<ServiceCardDto> getServiceCardsById(@PathVariable Long id) {
        return serviceCardService.getAllByMasterCardId(id);
    }

    @PostMapping("/servicecard")
    public ServiceCardDto createServiceCard(
            @RequestBody
            @Valid
            ServiceCardCreateDto serviceCardCreateDto) {
        return serviceCardService.createServiceCard(serviceCardCreateDto);
    }
}
