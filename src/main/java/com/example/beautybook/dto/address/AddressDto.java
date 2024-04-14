package com.example.beautybook.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddressDto {
    @NotNull(message = "Сan not be empty")
    private Long cityId;
    @NotBlank(message = "Сan not be empty")
    private String street;
    @NotBlank(message = "Сan not be empty")
    private String houseNumber;
    private String description;
}
