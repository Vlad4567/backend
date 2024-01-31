package com.example.beautybook.dto.user;

import com.example.beautybook.model.Role;
import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private Role role;
}
