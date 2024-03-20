package com.example.beautybook.dto.mastercard;

import com.example.beautybook.dto.address.AddressResponseDto;
import com.example.beautybook.dto.category.SubcategoryResponseDto;
import com.example.beautybook.dto.contact.ContactDto;
import java.util.List;
import lombok.Data;

@Data
public class MasterCardCreateResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private ContactDto contacts;
    private AddressResponseDto address;
    private String description;
    private List<SubcategoryResponseDto> subcategories;
}
