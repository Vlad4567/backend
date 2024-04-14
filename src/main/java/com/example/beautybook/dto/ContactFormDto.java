package com.example.beautybook.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactFormDto(
        @Email(message = "Invalid email format.")
        @NotBlank(message = "Сan not be empty")
        String email,
        @NotBlank(message = "Сan not be empty")
        String message
) {
}
