package com.example.beautybook.repository.spec.mastercard;

import com.example.beautybook.dto.search.Param;
import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class NameMCSpecification implements SpecificationProvider<MasterCard> {
    @Override
    public String getKey() {
        return "name";
    }

    @Override
    public Specification<MasterCard> getSpecification(Param param) {
        if (param.text().split(" ").length == 1) {
            return getSingleWordSpecification(param.text());
        }

        String[] firstLastName = param.text().split(" ");
        return getTwoWordsSpecification(firstLastName[0], firstLastName[1]);
    }

    private Specification<MasterCard> getSingleWordSpecification(String text) {
        return (root, query, criteriaBuilder) -> {
            Predicate firstName = criteriaBuilder.like(
                    root.get("firstName"), "%" + text + "%");
            Predicate lastName = criteriaBuilder.like(
                    root.get("lastName"), "%" + text + "%");
            return criteriaBuilder.or(firstName, lastName);
        };
    }

    private Specification<MasterCard> getTwoWordsSpecification(String firstName, String lastName) {
        return (root, query, criteriaBuilder) -> {
            Predicate condition1 = criteriaBuilder.and(
                    criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%"),
                    criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%")
            );
            Predicate condition2 = criteriaBuilder.and(
                    criteriaBuilder.like(root.get("firstName"), "%" + lastName + "%"),
                    criteriaBuilder.like(root.get("lastName"), "%" + firstName + "%")
            );
            return criteriaBuilder.or(condition1, condition2);
        };
    }
}
