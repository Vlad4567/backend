package com.example.beautybook.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubcategoryDto {
    @NotBlank
    private String name;
    @NotNull
    private Long categoryId;
}