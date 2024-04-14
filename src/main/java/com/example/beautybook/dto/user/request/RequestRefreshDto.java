package com.example.beautybook.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public record RequestRefreshDto(@NotBlank(message = "Сan not be empty") String refreshToken) {
}
