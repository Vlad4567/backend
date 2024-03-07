package com.example.beautybook.dto.user;

import jakarta.validation.constraints.NotBlank;

public record RequestRefreshDto(@NotBlank String refreshToken) {
}
