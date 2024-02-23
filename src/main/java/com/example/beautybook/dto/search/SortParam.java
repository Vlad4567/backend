package com.example.beautybook.dto.search;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Sort;

public record SortParam(
        @NotBlank
        Sort.Direction direction,
        @NotBlank
        String property
) {
}
