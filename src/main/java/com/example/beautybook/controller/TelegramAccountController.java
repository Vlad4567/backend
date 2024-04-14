package com.example.beautybook.controller;

import com.example.beautybook.dto.ContactFormDto;
import com.example.beautybook.service.ContactFormService;
import com.example.beautybook.service.TelegramAccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(
        origins = "*",
        allowedHeaders = {"Content-Type", "Authorization"})
@RequiredArgsConstructor
@RestController
public class TelegramAccountController {
    private final ContactFormService contactFormService;
    private final TelegramAccountService telegramAccountService;

    @PostMapping("/contactUs")
    public void contactUs(@RequestBody @Valid ContactFormDto contactFormDto) {
        contactFormService.contactUs(contactFormDto);
    }

    @PostMapping("/verificationTelegram/{code}")
    public String verificationTelegram(
            @PathVariable
            @NotNull(message = "Сan not be empty")
            Long code
    ) {
        return telegramAccountService.verificationTelegramAccount(code);
    }

    @DeleteMapping("/telegram/account")
    public void deleteTelegramAccount() {
        telegramAccountService.sendRequestForDeletion();
    }
}
