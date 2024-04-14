package com.example.beautybook.dto.mastercard;

import com.example.beautybook.dto.address.AddressResponseDto;
import com.example.beautybook.dto.category.SubcategoryResponseDto;
import com.example.beautybook.dto.contact.ContactDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class MasterCardDto {
    private Long id;
    private String firstName;
    private String lastName;
    private AddressResponseDto address;
    private ContactDto contacts;
    private BigDecimal rating;
    private String description;
    private String mainPhoto;
    private List<SubcategoryResponseDto> subcategories;
    private Map<String, Long> statistics;
    private boolean isHidden;
}
