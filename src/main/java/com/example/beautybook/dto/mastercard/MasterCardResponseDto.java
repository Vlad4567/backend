package com.example.beautybook.dto.mastercard;

import com.example.beautybook.dto.address.AddressResponseDto;
import com.example.beautybook.dto.category.SubcategoryDto;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class MasterCardResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private AddressResponseDto address;
    private Set<SubcategoryDto> subcategories;
    private BigDecimal rating;
    private String mainPhoto;
    private boolean isFavorite;
}
