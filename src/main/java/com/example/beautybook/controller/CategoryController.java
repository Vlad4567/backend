package com.example.beautybook.controller;

import com.example.beautybook.dto.category.CategoryDto;
import com.example.beautybook.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/categories")
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getAll() {
        return categoryService.getAllCategory();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/categories/{id}")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }
}
