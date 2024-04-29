package com.example.beautybook.service.impl;

import com.example.beautybook.dto.category.SubcategoryDto;
import com.example.beautybook.dto.category.SubcategoryResponseDto;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.mapper.SubcategoryMapper;
import com.example.beautybook.model.Subcategory;
import com.example.beautybook.repository.categoty.SubcategoryRepository;
import com.example.beautybook.service.SubcategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubcategoryServiceImpl implements SubcategoryService {
    private final SubcategoryRepository subcategoryRepository;
    private final SubcategoryMapper subcategoryMapper;

    @Override
    @Transactional
    public SubcategoryDto save(SubcategoryDto subcategoryCreateDto) {
        Subcategory subcategory = subcategoryMapper.toModel(subcategoryCreateDto);
        return subcategoryMapper.toDto(subcategoryRepository.save(subcategory));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubcategoryResponseDto> getAllByCategory(Long id) {
        return subcategoryRepository.findAllByCategoryId(id).stream()
                .map(subcategoryMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public SubcategoryDto update(Long id, SubcategoryDto subcategoryDto) {
        if (subcategoryRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Not found subcategory by id: " + id);
        }
        Subcategory subcategory = subcategoryMapper.toModel(subcategoryDto);
        subcategory.setId(id);

        return subcategoryMapper.toDto(subcategoryRepository.save(subcategory));
    }
}
