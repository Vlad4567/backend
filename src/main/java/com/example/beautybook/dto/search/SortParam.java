package com.example.beautybook.dto.search;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;

public record SortParam(
        @NotNull
        Sort.Direction direction,
        @NotBlank
        String property
) {
}
