package com.example.beautybook.bot.telegram.command.callbackquery;

import com.example.beautybook.bot.Command;
import com.example.beautybook.bot.telegram.TelegramBot;
import com.example.beautybook.bot.telegram.provider.CallbackQueryTelegramCommandProvider;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class CancelTelegramCommand implements Command<CallbackQueryTelegramCommandProvider> {
    @Override
    public String getKey() {
        return "#cancel";
    }

    @Override
    public void executeCommand(Object o, Object bot) {
        if (o instanceof Update update && bot instanceof TelegramBot telegramBot) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            telegramBot.send(SendMessage.builder().chatId(chatId).text("Canceled").build());
        }
    }
}
