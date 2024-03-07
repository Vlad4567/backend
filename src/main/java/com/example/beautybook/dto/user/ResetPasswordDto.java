package com.example.beautybook.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPasswordDto {
    @NotBlank
    private String currentPassword;
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "The password must use at least 8 characters, including a number, "
                    + "a lowercase letter, an uppercase letter, and a special character."
    )
    private String newPassword;
}
