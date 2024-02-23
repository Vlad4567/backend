package com.example.beautybook.repository.spec.servicecard;

import com.example.beautybook.dto.search.Param;
import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.model.ServiceCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class NameSCSpecification implements SpecificationProvider<ServiceCard> {
    @Override
    public String getKey() {
        return "name";
    }

    @Override
    public Specification<ServiceCard> getSpecification(Param param) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + param.text() + "%");
    }
}
