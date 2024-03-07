package com.example.beautybook.repository.spec.mastercard;

import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class NameMcSpecification implements SpecificationProvider<MasterCard> {
    @Override
    public String getKey() {
        return "name";
    }

    @Override
    public Specification<MasterCard> getSpecification(SearchParam param) {
        if (param.getText().split(" ").length == 1) {
            return getSingleWordSpecification(param.getText());
        }

        String[] firstLastName = param.getText().split(" ");
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
            Predicate predicate1 = criteriaBuilder.and(
                    criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%"),
                    criteriaBuilder.like(root.get("lastName"), "%" + lastName + "%"));
            Predicate predicate2 = criteriaBuilder.and(
                    criteriaBuilder.like(root.get("lastName"), "%" + firstName + "%"),
                    criteriaBuilder.like(root.get("firstName"), "%" + lastName + "%"));
            Predicate predicate3 = criteriaBuilder.like(
                    root.get("firstName"), "%" + firstName + " " + lastName + "%");
            return criteriaBuilder.or(predicate1, predicate2, predicate3);
        };
    }
}
