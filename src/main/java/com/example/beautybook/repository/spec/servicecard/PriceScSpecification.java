package com.example.beautybook.repository.spec.servicecard;

import com.example.beautybook.dto.search.SearchParam;
import com.example.beautybook.model.ServiceCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceScSpecification implements SpecificationProvider<ServiceCard> {
    @Override
    public String getKey() {
        return "price";
    }

    @Override
    public Specification<ServiceCard> getSpecification(SearchParam param) {
        BigDecimal[] minMaxPrice = getMinAndMaxPrice(param.getMinPrice(), param.getMaxPrice());
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("price"), minMaxPrice[0], minMaxPrice[1]);
    }

    private BigDecimal[] getMinAndMaxPrice(String minPrice, String maxPrice) {
        BigDecimal[] minMaxPrice = new BigDecimal[]{
                new BigDecimal(0),
                new BigDecimal(Integer.MAX_VALUE)
        };
        if (!minPrice.isBlank()) {
            minMaxPrice[0] = new BigDecimal(minPrice);
        }
        if (!maxPrice.isBlank()) {
            minMaxPrice[1] = new BigDecimal(maxPrice);
        }
        return minMaxPrice;
    }
}
