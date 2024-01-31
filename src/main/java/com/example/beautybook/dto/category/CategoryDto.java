package com.example.beautybook.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryDto(@NotNull String name, String description) {
}
