package com.example.beautybook.service.impl;

import com.example.beautybook.repository.address.AddressRepository;
import com.example.beautybook.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
}
