package com.example.beautybook.service.impl;

import com.example.beautybook.bot.telegram.TelegramBot;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.exceptions.TelegramException;
import com.example.beautybook.message.MessageProvider;
import com.example.beautybook.model.TelegramAccount;
import com.example.beautybook.model.TelegramCode;
import com.example.beautybook.model.User;
import com.example.beautybook.repository.TelegramAccountRepository;
import com.example.beautybook.repository.TelegramCodeRepository;
import com.example.beautybook.repository.user.UserRepository;
import com.example.beautybook.service.TelegramAccountService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Service
@RequiredArgsConstructor
public class TelegramAccountServiceImpl implements TelegramAccountService {
    private final TelegramAccountRepository telegramAccountRepository;
    private final UserRepository userRepository;
    private final TelegramBot telegramBot;
    private final TelegramCodeRepository codeRepository;

    @Override
    @Transactional
    public void sendRequestForDeletion() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUuid(authentication.getName()).orElseThrow(
                () -> new EntityNotFoundException("Not found user by uuid"));
        if (user.getTelegramAccount() == null) {
            throw new EntityNotFoundException("Not found telegram account");
        }
        if (user.getEmail() == null) {
            throw new TelegramException("You cannot delete your telegram account, "
                    + "there is no alternative authentication method");
        }
        String text = MessageProvider.getMessage("delete.telegram", user.getUserName());
        telegramBot.send(
                SendMessage.builder()
                        .chatId(user.getTelegramAccount().getChatId())
                        .text(text)
                        .replyMarkup(getInlineKeyboardMarkup())//TODO
                        .build()
        );
    }

    @Override
    @Transactional
    public String verificationTelegramAccount(Long code) {
        Optional<TelegramCode> telegramCode = codeRepository.findById(code);
        if (telegramCode.isEmpty()) {
            throw new TelegramException("The code is invalid");
        }

        codeRepository.deleteById(code);
        String username = telegramCode.get().getUserName();
        Long chatId = telegramCode.get().getChatId();

        if (telegramAccountRepository.existsByChatId(chatId)) {
            throw new TelegramException("An account with this chat id: " + chatId
                    + " is already linked to another user.");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUuid(authentication.getName()).orElseThrow(
                () -> new EntityNotFoundException("Not found user by uuid"));
        user.setTelegramAccount(new TelegramAccount(username, chatId));
        userRepository.save(user);
        return username;
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(
                        List.of(
                                InlineKeyboardButton.builder()
                                        .text("Confirm")
                                        .callbackData("#confirm:delete")
                                        .build(),
                                InlineKeyboardButton.builder()
                                        .text("Cancel")
                                        .callbackData("#cancel")
                                        .build()
                        )
                )
                .build();
    }
}
