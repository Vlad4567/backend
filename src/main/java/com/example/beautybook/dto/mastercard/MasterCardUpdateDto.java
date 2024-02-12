package com.example.beautybook.dto.mastercard;

import com.example.beautybook.dto.location.LocationDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MasterCardUpdateDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String address;
    @NotNull
    private LocationDto location;
    private String description;
}
