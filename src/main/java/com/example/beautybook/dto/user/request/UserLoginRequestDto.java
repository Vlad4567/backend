package com.example.beautybook.dto.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotNull(message = "Сan not be empty")
    private String email;
    @NotNull(message = "Сan not be empty")
    private String password;
    private boolean remember;
}
