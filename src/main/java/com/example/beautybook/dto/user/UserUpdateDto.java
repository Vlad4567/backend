package com.example.beautybook.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserUpdateDto {
    @Email
    @NotNull
    private String email;
    @Pattern(
            regexp = "^\\+?[0-9]+( [0-9]+)*$",
            message = "Invalid phone number format."
    )
    private String phone;
    @NotNull
    private String username;
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "The password must use at least 8 characters, including a number, "
                    + "a lowercase letter, an uppercase letter, and a special character."
    )
    private String password;
}
