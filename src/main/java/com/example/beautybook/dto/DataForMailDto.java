package com.example.beautybook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataForMailDto {
    private String username;
    private String email;
    private String newData;
    private String emailType;

    public DataForMailDto(String username, String email, String emailType) {
        this.username = username;
        this.email = email;
        this.emailType = emailType;
    }
}
