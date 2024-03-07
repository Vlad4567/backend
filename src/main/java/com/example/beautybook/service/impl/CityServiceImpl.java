package com.example.beautybook.service.impl;

import com.example.beautybook.model.City;
import com.example.beautybook.repository.CityRepository;
import com.example.beautybook.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Override
    public Page<City> searchCity(String text, Pageable pageable) {
        return cityRepository.searchCityName(text, pageable);
    }
}
