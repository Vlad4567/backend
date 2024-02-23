package com.example.beautybook.repository.user;

import com.example.beautybook.dto.search.Param;
import com.example.beautybook.dto.search.SearchParam;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(Param param);
}
