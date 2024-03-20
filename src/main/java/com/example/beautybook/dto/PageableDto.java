package com.example.beautybook.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class PageableDto {
    private int page;
    private int size;
    private Sort.Direction direction;
    private String property;
}
