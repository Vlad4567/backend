package com.example.beautybook.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateDto {
    @Email(message = "Invalid email format.")
    @NotBlank(message = "Сan not be empty")
    private String email;
    @NotBlank(message = "Сan not be empty")
    private String username;
}
