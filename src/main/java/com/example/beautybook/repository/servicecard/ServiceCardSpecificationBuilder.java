package com.example.beautybook.repository.servicecard;

import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.model.ServiceCard;
import com.example.beautybook.repository.user.SpecificationBuilder;
import com.example.beautybook.repository.user.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceCardSpecificationBuilder implements SpecificationBuilder<ServiceCard> {
    private final SpecificationProviderManager<ServiceCard> providerManager;

    @Override
    public Specification<ServiceCard> build(SearchParam param) {
        Specification<ServiceCard> spec = Specification.where(null);

        if (param.getText() != null && !param.getText().isBlank()) {
            spec = spec.and(
                    providerManager.getSpecificationProvider("name")
                            .getSpecification(param));
        }

        if (param.getCity() != null) {
            spec = spec.and(
                    providerManager.getSpecificationProvider("city")
                            .getSpecification(param));
        }

        if (param.getSubcategories() != null) {
            spec = spec.and(
                    providerManager.getSpecificationProvider("subcategory")
                            .getSpecification(param));
        }

        return spec.and(
                providerManager.getSpecificationProvider("price")
                        .getSpecification(param));
    }
}
