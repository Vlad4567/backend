package com.example.beautybook.repository.spec.servicecard;

import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.model.ServiceCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AddressCityScSpecification implements SpecificationProvider<ServiceCard> {
    @Override
    public String getKey() {
        return "city";
    }

    @Override
    public Specification<ServiceCard> getSpecification(SearchParam param) {
        return (root, query, criteriaBuilder) ->
                root
                        .join("masterCard")
                        .join("address")
                        .join("city")
                        .get("id")
                        .in(param.getCity());
    }
}
