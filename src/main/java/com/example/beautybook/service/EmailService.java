package com.example.beautybook.service;

import com.example.beautybook.model.User;

public interface EmailService {
    void sendMail(User user, String emailType);
}
