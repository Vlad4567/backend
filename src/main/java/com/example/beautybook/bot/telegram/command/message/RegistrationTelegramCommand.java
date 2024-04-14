package com.example.beautybook.bot.telegram.command.message;

import com.example.beautybook.bot.Command;
import com.example.beautybook.bot.telegram.TelegramBot;
import com.example.beautybook.bot.telegram.provider.MessageTelegramCommandProvider;
import com.example.beautybook.message.MessageProvider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
@RequiredArgsConstructor
public class RegistrationTelegramCommand implements Command<MessageTelegramCommandProvider> {
    @Override
    public String getKey() {
        return "Registration";
    }

    @Override
    public void executeCommand(Object o, Object bot) {
        if (o instanceof Update update && bot instanceof TelegramBot telegramBot) {
            Long chatId = update.getMessage().getChatId();
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .replyMarkup(createInlineKeyboardMarkup())
                    .text(MessageProvider.getMessage("confirm.registration"))
                    .build();
            telegramBot.send(sendMessage);
        }
    }

    private InlineKeyboardMarkup createInlineKeyboardMarkup() {
        return InlineKeyboardMarkup.builder().keyboardRow(
                List.of(
                        InlineKeyboardButton.builder()
                                .text("Confirm")
                                .callbackData("#confirm:registration")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("Cancel")
                                .callbackData("#cancel")
                                .build()
                )
        ).build();
    }
}
