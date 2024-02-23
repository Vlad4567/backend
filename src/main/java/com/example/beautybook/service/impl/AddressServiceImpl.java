package com.example.beautybook.service.impl;

import com.example.beautybook.repository.address.AddressRepository;
import com.example.beautybook.repository.address.AddressSpecification;
import com.example.beautybook.service.AddressService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    @Override
    public Page<String> findAllCity(String city, Pageable pageable) {
        return addressRepository.findDistinctCity(city, pageable);
    }
}
