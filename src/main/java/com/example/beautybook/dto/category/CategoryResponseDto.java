package com.example.beautybook.dto.category;

import java.util.Set;
import lombok.Data;

@Data
public class CategoryResponseDto {
    private Long id;
    private String name;
    private Set<SubcategoryResponseDto> subcategories;
}
