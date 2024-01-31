package com.example.beautybook.dto.user;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @Email
    @NotNull
    private String email;
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "The password must use at least 8 characters, including a number, "
                    + "a lowercase letter, an uppercase letter, and a special character."
    )
    private String password;
    @Transient
    private String repeatPassword;
    @NotNull
    private String username;
    private String firstName;
    private String lastName;

    @AssertTrue(message = "Password mismatch")
    public boolean isPasswordMatching() {
        if (password != null) {
            return password.equals(repeatPassword);
        }
        return false;
    }
}
