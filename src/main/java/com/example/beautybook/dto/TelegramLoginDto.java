package com.example.beautybook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TelegramLoginDto(
        @NotNull(message = "Сan not be empty")
        Long code,
        @NotNull(message = "Сan not be empty")
        @NotBlank(message = "Сan not be empty")
        String token
) {
}
