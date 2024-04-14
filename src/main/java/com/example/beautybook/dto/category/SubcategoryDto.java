package com.example.beautybook.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SubcategoryDto {
    @NotBlank(message = "Сan not be empty")
    private String name;
    @NotNull(message = "Сan not be empty")
    private Long categoryId;
}
