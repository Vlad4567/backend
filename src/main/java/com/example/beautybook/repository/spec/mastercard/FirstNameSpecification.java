package com.example.beautybook.repository.spec.mastercard;

import com.example.beautybook.dto.SearchParam;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class FirstNameSpecification implements SpecificationProvider<MasterCard> {
    @Override
    public String getKey() {
        return "firstName";
    }

    @Override
    public Specification<MasterCard> getSpecification(SearchParam param) {
        return (root, query, criteriaBuilder) -> {

            Predicate[] predicates = Arrays.stream(param.text())
                    .map(firstName ->
                            criteriaBuilder.like(
                                    root.get("firstName"),
                                    "%" + firstName + "%"
                            )
                    )
                    .toArray(Predicate[]::new);
            return criteriaBuilder.or(predicates);
        };
    }
}
