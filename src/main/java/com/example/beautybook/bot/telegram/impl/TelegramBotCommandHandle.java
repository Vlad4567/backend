package com.example.beautybook.bot.telegram.impl;

import com.example.beautybook.bot.BotCommandHandler;
import com.example.beautybook.bot.Command;
import com.example.beautybook.bot.CommandProviderManager;
import com.example.beautybook.bot.telegram.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class TelegramBotCommandHandle implements BotCommandHandler<TelegramBot> {
    private final CommandProviderManager<TelegramBot> providerManager;
    @Value("${telegram.admin.chat}")
    private Long adminChatId;

    @Override
    public void handleCommand(Object object, TelegramBot bot) {
        Update update = (Update) object;
        String key = getKey(update);
        if (key == null) {
            return;
        }

        Command<?> command = providerManager.getCommandProvider(key).getCommand(update);
        if (command == null) {
            return;
        }
        command.executeCommand(update, bot);
    }

    private String getKey(Update update) {
        if (update.hasMessage()
                && update.getMessage().getChatId().equals(adminChatId)
                && update.getMessage().isReply()) {
            return "reply";
        }
        if (update.hasMessage()) {
            return "message";
        }
        if (update.hasCallbackQuery()) {
            return "callbackQuery";
        }
        return null;
    }
}
