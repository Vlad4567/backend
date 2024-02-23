package com.example.beautybook.controller;

import com.example.beautybook.service.AddressService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/city")
    public Page<String> getCity(String city, Pageable pageable) {
        return addressService.findAllCity(city,pageable);
    }
}
