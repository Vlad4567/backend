package com.example.beautybook.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressService {
    Page<String> findAllCity(String city, Pageable pageable);
}
