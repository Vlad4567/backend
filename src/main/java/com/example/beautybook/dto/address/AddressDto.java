package com.example.beautybook.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressDto {
    @NotNull
    private Long cityId;
    @NotBlank
    private String street;
    @NotBlank
    private String houseNumber;
    private String description;
}
