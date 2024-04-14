package com.example.beautybook.dto.address;

import lombok.Data;

@Data
public class AddressResponseDto {
    private String city;
    private String street;
    private String houseNumber;
    private String description;
}
