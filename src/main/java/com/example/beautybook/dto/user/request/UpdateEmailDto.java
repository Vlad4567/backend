package com.example.beautybook.dto.user.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateEmailDto {
    @Email(message = "Invalid email format.")
    private String newEmail;
    private String password;
}
