package com.example.beautybook.repository.spec.servicecard;

import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.model.ServiceCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SubCategoryScSpecification implements SpecificationProvider<ServiceCard> {
    @Override
    public String getKey() {
        return "subcategory";
    }

    @Override
    public Specification<ServiceCard> getSpecification(SearchParam param) {
        return (root, query, criteriaBuilder) -> {
            Long[] subcategoryIds = param.getSubcategories();
            Predicate[] predicates = new Predicate[subcategoryIds.length];

            for (int i = 0; i < subcategoryIds.length; i++) {
                Long id = subcategoryIds[i];
                predicates[i] = criteriaBuilder.equal(
                        root.join("subcategory", JoinType.LEFT).get("id"), id);
            }
            return criteriaBuilder.or(predicates);
        };
    }
}
