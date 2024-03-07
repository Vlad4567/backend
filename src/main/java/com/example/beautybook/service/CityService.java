package com.example.beautybook.service;

import com.example.beautybook.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityService {
    Page<City> searchCity(String text, Pageable pageable);
}
