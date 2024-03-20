package com.example.beautybook.dto.address;

import com.example.beautybook.model.City;
import lombok.Data;

@Data
public class AddressResponseDto {
    private City city;
    private String street;
    private String houseNumber;
}
