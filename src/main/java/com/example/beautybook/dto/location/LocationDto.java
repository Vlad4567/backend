package com.example.beautybook.dto.location;

import jakarta.validation.constraints.NotNull;

public record LocationDto(
        @NotNull(message = "Сan not be empty")
        Double latitude,
        @NotNull(message = "Сan not be empty")
        Double longitude
) {
}
