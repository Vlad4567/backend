package com.example.beautybook.controller;

import com.example.beautybook.dto.category.SubcategoryDto;
import com.example.beautybook.dto.category.SubcategoryResponseDto;
import com.example.beautybook.service.SubcategoryService;
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
public class SubcategoryController {
    private final SubcategoryService subcategoryService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/subcategories")
    public SubcategoryDto createSubcategory(
            @RequestBody
            @Valid
            SubcategoryDto subcategoryDto
    ) {
        return subcategoryService.save(subcategoryDto);
    }

    @GetMapping("/subcategories")
    public List<SubcategoryResponseDto> getAllByCategory(Long id) {
        return subcategoryService.getAllByCategory(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/subcategories/{id}")
    public SubcategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody @Valid SubcategoryDto subcategoryDto) {
        return subcategoryService.update(id, subcategoryDto);
    }
}
