package com.example.beautybook.repository.spec.mastercard;

import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SubCategoryMcSpecification implements SpecificationProvider<MasterCard> {
    @Override
    public String getKey() {
        return "subcategory";
    }

    @Override
    public Specification<MasterCard> getSpecification(SearchParam params) {
        return (root, query, criteriaBuilder) -> {
            Long[] subcategoryIds = params.getSubcategories();
            Predicate[] predicates = new Predicate[subcategoryIds.length];

            for (int i = 0; i < subcategoryIds.length; i++) {
                predicates[i] = root
                        .join("subcategories")
                        .get("id")
                        .in(subcategoryIds[i]);
            }
            return criteriaBuilder.or(predicates);
        };
    }
}
