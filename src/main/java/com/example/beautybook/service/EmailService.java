package com.example.beautybook.service;

import com.example.beautybook.dto.DataForMailDto;

public interface EmailService {
    void sendMail(DataForMailDto dto);
}
