package com.example.beautybook.dto.mastercard;

import com.example.beautybook.dto.address.AddressDto;
import com.example.beautybook.dto.location.LocationDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MasterCardUpdateDto {
    @NotBlank
    private String firstName;
    private String lastName;
    @NotNull
    private AddressDto address;
    private LocationDto location;
    private String description;
}
