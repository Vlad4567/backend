package com.example.beautybook.repository.spec.servicecard;

import com.example.beautybook.dto.search.Param;
import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.model.ServiceCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AddressCitySCSpecification implements SpecificationProvider<ServiceCard> {
    @Override
    public String getKey() {
        return "city";
    }

    @Override
    public Specification<ServiceCard> getSpecification(Param param) {
        return (root, query, criteriaBuilder) ->
                root
                        .join("masterCard")
                        .join("address")
                        .get("city")
                        .in(param.city());
    }
}
