package com.example.beautybook.controller;

import com.example.beautybook.model.City;
import com.example.beautybook.service.CityService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(
        origins = "*",
        allowedHeaders = {"Content-Type", "Authorization"})
@RequiredArgsConstructor
@RestController
public class CityController {
    private final CityService cityService;

    @GetMapping("/city/search")
    public Page<City> searchCity(
            @NotBlank(message = "Сan not be empty")
            String text,
            Pageable pageable
    ) {
        return cityService.searchCity(text, pageable);
    }
}
