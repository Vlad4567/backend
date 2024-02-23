package com.example.beautybook.dto.address;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressDto {
    @NotBlank
    private String city;
    private String street;
    private String houseNumber;
}
