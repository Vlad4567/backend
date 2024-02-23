package com.example.beautybook.repository.spec.servicecard;

import com.example.beautybook.dto.search.Param;
import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.model.ServiceCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SubCategorySCSpecification implements SpecificationProvider<ServiceCard> {
    @Override
    public String getKey() {
        return "subcategory";
    }

    @Override
    public Specification<ServiceCard> getSpecification(Param param) {
        return (root, query, criteriaBuilder) ->
                root
                        .join("subcategory")
                        .get("name")
                        .in(param.subcategory());
    }
}
