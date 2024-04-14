package com.example.beautybook.service;

public interface TelegramAccountService {
    void sendRequestForDeletion();

    String verificationTelegramAccount(Long code);
}
