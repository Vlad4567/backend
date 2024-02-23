package com.example.beautybook.repository.spec.mastercard;

import com.example.beautybook.dto.search.Param;
import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CategoryMCSpecification implements SpecificationProvider<MasterCard> {
    @Override
    public String getKey() {
        return "category";
    }

    @Override
    public Specification<MasterCard> getSpecification(Param params) {
        return (root, query, criteriaBuilder) ->
                root
                        .join("subcategories")
                        .join("category")
                        .get("name")
                        .in(params.category());
    }
}
