package com.example.beautybook.bot.telegram;

import com.example.beautybook.bot.BotCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotCommandHandler<TelegramBot> handler;

    @Autowired
    public TelegramBot(@Value("${telegram.bot.token}") String botToken,
                       BotCommandHandler<TelegramBot> handler) {
        super(botToken);
        this.handler = handler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        handler.handleCommand(update, this);
    }

    @Override
    public String getBotUsername() {
        return "SparkleServiceBot";
    }

    public int send(BotApiMethodMessage message) {
        try {
            return execute(message).getMessageId();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMessage(Long chatId, int idMessage) {
        DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(chatId), idMessage);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void updateTextMessage(Long chatId, int messageId, String text) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(text);
        editMessageText.enableMarkdown(true);

        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
