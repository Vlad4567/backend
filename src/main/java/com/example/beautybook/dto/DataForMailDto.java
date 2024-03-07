package com.example.beautybook.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataForMailDto {
    private String username;
    private String email;
    private String newEmail;
    private String newPassword;
    private String emailType;

    public DataForMailDto(String username, String email, String emailType) {
        this.username = username;
        this.email = email;
        this.emailType = emailType;
    }

    public DataForMailDto(String username, String email, String newPassword, String emailType) {
        this.username = username;
        this.email = email;
        this.newPassword = newPassword;
        this.emailType = emailType;
    }
}
