package com.example.beautybook.dto.search;

public record Param(String text,
                    String city,
                    String category,
                    String subcategory,
                    String minPrice,
                    String maxPrice
) {
}
