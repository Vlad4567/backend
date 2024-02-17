package com.example.beautybook.repository.spec.mastercard;

import com.example.beautybook.dto.SearchParam;
import com.example.beautybook.model.MasterCard;
import com.example.beautybook.model.ServiceCard;
import com.example.beautybook.repository.user.SpecificationProvider;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceAndCategorySpecification implements SpecificationProvider<MasterCard> {
    @Override
    public String getKey() {
        return "priceAndCategory";
    }

    @Override
    public Specification<MasterCard> getSpecification(SearchParam params) {
        BigDecimal[] minMaxPrice = getMinAndMaxPrice(params.price());
        return (root, query, criteriaBuilder) -> {
            Join<MasterCard, ServiceCard> join = root.join("serviceCards");
            Predicate predicate = criteriaBuilder.between(
                    join.get("price"),
                    minMaxPrice[0],
                    minMaxPrice[1]
            );

            if (params.category() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(
                                join.join("subcategory")
                                        .join("category").get("id"),
                                params.category()[0]
                        )
                );
            }
            if (params.sort() != null && params.sort()[0].equalsIgnoreCase("price")) {
                if (params.sort()[1].equalsIgnoreCase("asc")) {
                    query.orderBy(criteriaBuilder.asc(join.get("price")));
                } else {
                    query.orderBy(criteriaBuilder.desc(join.get("price")));
                }
            }
            return predicate;
        };
    }

    private BigDecimal[] getMinAndMaxPrice(String[] prices) {
        BigDecimal[] minMaxPrice = new BigDecimal[]{
                new BigDecimal(0),
                new BigDecimal(Integer.MAX_VALUE)
        };
        if (prices == null) {
            return minMaxPrice;
        }
        for (int i = 0; i < 2; i++) {
            if (prices[i] != null && !prices[i].isBlank()) {
                minMaxPrice[i] = new BigDecimal(prices[i]);
            }
        }
        return minMaxPrice;
    }
}
