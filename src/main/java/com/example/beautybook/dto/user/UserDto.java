package com.example.beautybook.dto.user;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String username;
    private String profilePhoto;
    private boolean master;
}
