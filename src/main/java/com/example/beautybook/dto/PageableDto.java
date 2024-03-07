package com.example.beautybook.dto;

import org.springframework.data.domain.Sort;

public record PageableDto(int page, int size, Sort.Direction direction, String property) {
}
