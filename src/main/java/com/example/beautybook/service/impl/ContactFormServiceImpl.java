package com.example.beautybook.service.impl;

import com.example.beautybook.bot.telegram.TelegramBot;
import com.example.beautybook.dto.ContactFormDto;
import com.example.beautybook.message.MessageProvider;
import com.example.beautybook.model.ContactForm;
import com.example.beautybook.repository.ContactFormRepository;
import com.example.beautybook.service.ContactFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class ContactFormServiceImpl implements ContactFormService {
    private final TelegramBot telegramBot;
    private final ContactFormRepository contactFormRepository;
    @Value("${telegram.admin.chat}")
    private Long adminChatId;

    @Override
    @Transactional
    public void contactUs(ContactFormDto contactFormDto) {
        ContactForm contactForm = contactFormRepository.save(
                new ContactForm(contactFormDto.email(), contactFormDto.message())
        );
        String text = MessageProvider.getMessage(
                "contact.form.telegram",
                contactForm.getId(),
                contactForm.getEmail(),
                contactForm.getMessage()
        );
        telegramBot.send(
                SendMessage.builder()
                        .chatId(adminChatId)
                        .text(text)
                        .build()
        );
    }
}
