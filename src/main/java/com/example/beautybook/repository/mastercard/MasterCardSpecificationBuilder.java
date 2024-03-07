package com.example.beautybook.repository.mastercard;

import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.repository.user.SpecificationBuilder;
import com.example.beautybook.repository.user.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MasterCardSpecificationBuilder implements SpecificationBuilder<MasterCard> {
    private final SpecificationProviderManager<MasterCard> providerManager;

    @Override
    public Specification<MasterCard> build(SearchParam param) {
        Specification<MasterCard> spec = Specification.where(null);
        if (param.getText() != null && !param.getText().isBlank()) {
            spec = spec.and(
                    providerManager.getSpecificationProvider("name")
                            .getSpecification(param)
            );
        }

        if (param.getSubcategories() != null) {
            spec = spec.and(
                    providerManager.getSpecificationProvider("subcategory")
                            .getSpecification(param)
            );
        }

        if (param.getCity() != null) {
            spec = spec.and(
                    providerManager.getSpecificationProvider("city")
                            .getSpecification(param)
            );
        }
        return spec;
    }
}
