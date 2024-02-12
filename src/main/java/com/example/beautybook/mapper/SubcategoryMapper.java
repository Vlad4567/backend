package com.example.beautybook.mapper;

import com.example.beautybook.config.MapperConfig;
import com.example.beautybook.dto.category.SubcategoryDto;
import com.example.beautybook.dto.category.SubcategoryResponseDto;
import com.example.beautybook.model.Category;
import com.example.beautybook.model.Subcategory;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface SubcategoryMapper {
    @Mapping(target = "category", ignore = true)
    Subcategory toModel(SubcategoryDto subcategoryCreateDto);

    @Mapping(target = "categoryId", source = "category.id")
    SubcategoryDto toDto(Subcategory subcategory);

    SubcategoryResponseDto toResponseDto(Subcategory subcategory);

    @AfterMapping
    default void setCategoryByCategoryId(
            @MappingTarget Subcategory subcategory,
            SubcategoryDto dto) {
        Category category = new Category();
        category.setId(dto.getCategoryId());
        subcategory.setCategory(category);
    }

}
