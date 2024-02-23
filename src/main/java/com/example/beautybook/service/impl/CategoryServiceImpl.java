package com.example.beautybook.service.impl;

import com.example.beautybook.dto.category.CategoryDto;
import com.example.beautybook.dto.category.CategoryResponseDto;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.mapper.CategoryMapper;
import com.example.beautybook.model.Category;
import com.example.beautybook.model.Subcategory;
import com.example.beautybook.repository.categoty.CategoryRepository;
import com.example.beautybook.service.CategoryService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toModel(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found category by id: " + id));
        categoryMapper.updateCategoryFromDto(categoryDto, category);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponseDto> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toResponseDto)
                .toList();
    }
}
