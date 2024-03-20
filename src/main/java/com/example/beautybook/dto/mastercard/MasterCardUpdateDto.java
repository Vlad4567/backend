package com.example.beautybook.dto.mastercard;

import com.example.beautybook.dto.address.AddressDto;
import com.example.beautybook.dto.contact.ContactDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MasterCardUpdateDto {
    @NotBlank
    private String firstName;
    private String lastName;
    @NotNull
    @Valid
    private ContactDto contacts;
    @NotNull
    @Valid
    private AddressDto address;
    private String description;
}
