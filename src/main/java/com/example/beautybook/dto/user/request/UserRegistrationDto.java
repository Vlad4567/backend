package com.example.beautybook.dto.user.request;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email must not be blank")
    private String email;
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "The password must use at least 8 characters, including a number, "
                    + "a lowercase letter, an uppercase letter, and a special character."
    )
    private String password;
    @Transient
    private String repeatPassword;
    @NotBlank(message = "Сan not be empty")
    private String username;

    @AssertTrue(message = "Password mismatch")
    public boolean isPasswordMatching() {
        if (password != null) {
            return password.equals(repeatPassword);
        }
        return false;
    }
}
