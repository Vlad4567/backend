package com.example.beautybook.dto.search;

import lombok.Data;

@Data
public class SearchParam {
    private String text;
    private Long city;
    private Long[] subcategories;
    private String minPrice;
    private String maxPrice;
}
