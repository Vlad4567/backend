package com.example.beautybook.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserUpdateDto {
    private String firstName;
    private String lastName;
    @NotNull
    private String username;
}
