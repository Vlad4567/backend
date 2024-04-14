package com.example.beautybook.bot.telegram.command.callbackquery;

import com.example.beautybook.bot.Command;
import com.example.beautybook.bot.telegram.TelegramBot;
import com.example.beautybook.bot.telegram.provider.CallbackQueryTelegramCommandProvider;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ReplyReviewTelegramCommand implements Command<CallbackQueryTelegramCommandProvider> {
    @Override
    public String getKey() {
        return "#reply:review";
    }

    @Override
    public void executeCommand(Object o, Object bot) {
        if (o instanceof Update update && bot instanceof TelegramBot telegramBot) {
            telegramBot.send(
                    SendMessage.builder()
                            .chatId(update.getCallbackQuery().getMessage().getChatId())
                            .text("This function is currently unavailable")
                            .build());
        }
    }
}
