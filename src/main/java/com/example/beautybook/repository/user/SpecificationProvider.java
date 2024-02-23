package com.example.beautybook.repository.user;

import com.example.beautybook.dto.search.Param;
import com.example.beautybook.dto.search.SearchParam;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    Specification<T> getSpecification(Param param);
}
