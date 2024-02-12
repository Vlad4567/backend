package com.example.beautybook.repository;

import com.example.beautybook.dto.SearchParam;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(SearchParam searchParam);
}