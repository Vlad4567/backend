package com.example.beautybook.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String username;
}
