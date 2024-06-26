package com.example.beautybook.service;

import com.example.beautybook.dto.category.CategoryDto;
import com.example.beautybook.dto.category.CategoryResponseDto;
import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    List<CategoryResponseDto> getAllCategory();
}
