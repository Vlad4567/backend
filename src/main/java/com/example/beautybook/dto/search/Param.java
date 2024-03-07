package com.example.beautybook.dto.search;

public record Param(String text,
                    Long city,
                    Long[] subcategories,
                    String minPrice,
                    String maxPrice
) {
}
