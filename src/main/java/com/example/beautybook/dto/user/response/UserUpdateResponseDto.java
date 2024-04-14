package com.example.beautybook.dto.user.response;

import com.example.beautybook.dto.TelegramAccountDto;
import lombok.Data;

@Data
public class UserUpdateResponseDto {
    private Long id;
    private String email;
    private String username;
    private String profilePhoto;
    private TelegramAccountDto telegramAccount;
    private boolean master;
    private String newEmail;
}
