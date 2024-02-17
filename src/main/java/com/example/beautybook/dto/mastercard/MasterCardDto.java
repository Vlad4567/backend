package com.example.beautybook.dto.mastercard;

import com.example.beautybook.dto.address.AddressDto;
import com.example.beautybook.dto.category.SubcategoryDto;
import com.example.beautybook.dto.photo.PhotoDto;
import com.example.beautybook.dto.review.ReviewDto;
import com.example.beautybook.dto.servicecard.ServiceCardDto;
import com.example.beautybook.model.Location;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;

@Data
public class MasterCardDto {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private AddressDto address;
    private Location location;
    private PhotoDto mainPhoto;
    private Set<PhotoDto> gallery;
    private Set<ReviewDto> reviews;
    private BigDecimal rating;
    private String description;
    private Set<SubcategoryDto> subcategories;
    private Set<ServiceCardDto> serviceCards;
}
