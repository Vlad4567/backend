package com.example.beautybook.dto.mastercard;

import com.example.beautybook.dto.address.AddressDto;
import com.example.beautybook.dto.contact.ContactDto;
import com.example.beautybook.dto.servicecard.ServiceCardCreateDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class MasterCardCreateDto {
    @NotBlank
    private String firstName;
    private String lastName;
    @NotNull
    private ContactDto contacts;
    private AddressDto address;
    private String description;
    private List<Long> subcategories;
    private List<ServiceCardCreateDto> serviceCards;

}
