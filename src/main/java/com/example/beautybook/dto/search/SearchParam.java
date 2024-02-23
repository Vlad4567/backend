package com.example.beautybook.dto.search;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SearchParam(
        @Min(0)
        int page,
        @Min(0)
        int sizePage,
        Param param,
        SortParam sort
) {
}
