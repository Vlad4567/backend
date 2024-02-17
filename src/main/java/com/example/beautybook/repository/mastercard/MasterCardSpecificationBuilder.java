package com.example.beautybook.repository.mastercard;

import com.example.beautybook.dto.SearchParam;
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
    public Specification<MasterCard> build(SearchParam searchParam) {
        Specification<MasterCard> spec = Specification.where(null);
        if (searchParam.text() != null && !searchParam.text()[0].isBlank()) {
            spec = spec.or(
                    providerManager.getSpecificationProvider("firstName")
                            .getSpecification(searchParam))
                    .or(providerManager.getSpecificationProvider("lastName")
                            .getSpecification(searchParam))
                    .or(providerManager.getSpecificationProvider("subcategory")
                            .getSpecification(searchParam));
        }

        if (searchParam.price() != null || searchParam.category() != null) {
            spec = spec.and(
                    providerManager.getSpecificationProvider("priceAndCategory")
                            .getSpecification(searchParam));
        }
        if (searchParam.city() != null && searchParam.city().length > 0) {
            spec = spec.and(
                    providerManager.getSpecificationProvider("city")
                            .getSpecification(searchParam));
        }

        return spec;
    }
}
