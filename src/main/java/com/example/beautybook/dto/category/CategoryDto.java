package com.example.beautybook.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryDto(@NotNull(message = "Сan not be empty") String name, String description) {
}
