package com.example.beautybook.repository.address;

import com.example.beautybook.model.Address;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class AddressSpecification {

    public Specification<Address> getCitySpecification(String city) {
        return (root, query, criteriaBuilder) -> {
            if (StringUtils.hasText(city)) {
                return criteriaBuilder.like(root.get("city"), "%" + city + "%");
            } else {
                return criteriaBuilder.conjunction();
            }
        };
    }
}
