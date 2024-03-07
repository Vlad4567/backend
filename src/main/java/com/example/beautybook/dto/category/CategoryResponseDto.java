package com.example.beautybook.dto.category;

import java.util.List;
import lombok.Data;

@Data
public class CategoryResponseDto {
    private Long id;
    private String name;
    private List<SubcategoryResponseDto> subcategories;
}
