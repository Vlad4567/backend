package com.example.beautybook.service;

import com.example.beautybook.dto.category.SubcategoryDto;
import com.example.beautybook.dto.category.SubcategoryResponseDto;
import java.util.List;

public interface SubcategoryService {
    SubcategoryDto save(SubcategoryDto subcategoryCreateDto);

    List<SubcategoryResponseDto> getAllByCategory(Long id);

    SubcategoryDto update(Long id, SubcategoryDto subcategoryDto);
}
