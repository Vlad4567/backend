package com.example.beautybook.dto.mastercard;

import com.example.beautybook.dto.Statistics;
import com.example.beautybook.dto.address.AddressResponseDto;
import com.example.beautybook.dto.category.SubcategoryResponseDto;
import com.example.beautybook.dto.contact.ContactDto;
import com.example.beautybook.dto.photo.PhotoDto;
import java.math.BigDecimal;
import java.util.List;
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
    private PhotoDto mainPhoto;
    private List<PhotoDto> gallery;
    private List<SubcategoryResponseDto> subcategories;
    private Statistics statistics;
    private boolean isHidden;
}
