package com.example.beautybook.repository.spec.mastercard;

import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AddressCityMcSpecification implements SpecificationProvider<MasterCard> {
    @Override
    public String getKey() {
        return "city";
    }

    @Override
    public Specification<MasterCard> getSpecification(SearchParam param) {
        return (root, query, criteriaBuilder) ->
                root
                        .join("address")
                        .join("city")
                        .get("id")
                        .in(param.getCity());
    }
}
