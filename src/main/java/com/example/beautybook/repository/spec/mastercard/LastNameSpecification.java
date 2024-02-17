package com.example.beautybook.repository.spec.mastercard;

import com.example.beautybook.dto.SearchParam;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class LastNameSpecification implements SpecificationProvider<MasterCard> {
    @Override
    public String getKey() {
        return "lastName";
    }

    @Override
    public Specification<MasterCard> getSpecification(SearchParam param) {
        return (root, query, criteriaBuilder) -> {
            Predicate[] predicates = Arrays.stream(param.text())
                    .map(lastName ->
                            criteriaBuilder.like(
                                    root.get("lastName"),
                                    "%" + lastName + "%"
                            )
                    )
                    .toArray(Predicate[]::new);
            return criteriaBuilder.or(predicates);
        };
    }
}
