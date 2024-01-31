package com.example.beautybook.dto.location;

import jakarta.validation.constraints.NotNull;

public record LocationDto(@NotNull Double latitude, @NotNull Double longitude) {
}
