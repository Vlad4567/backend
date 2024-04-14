package com.example.beautybook.dto.mastercard;

import com.example.beautybook.dto.address.AddressDto;
import com.example.beautybook.dto.contact.ContactDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MasterCardUpdateDto {
    @NotBlank(message = "Сan not be empty")
    private String firstName;
    private String lastName;
    @NotNull(message = "Сan not be empty")
    @Valid
    private ContactDto contacts;
    @NotNull(message = "Сan not be empty")
    @Valid
    private AddressDto address;
    @Size(max = 500, message = "Description must be less than or equal to 500 characters")
    private String description;
}
