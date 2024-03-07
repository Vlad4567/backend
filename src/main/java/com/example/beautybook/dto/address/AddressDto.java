package com.example.beautybook.dto.address;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressDto {
    @NotBlank
    private Long cityId;
    @NotBlank
    private String street;
    @NotBlank
    private String houseNumber;
    private String description;
}
