package com.example.beautybook.bot.telegram.command.message;

import com.example.beautybook.bot.Command;
import com.example.beautybook.bot.telegram.TelegramBot;
import com.example.beautybook.bot.telegram.provider.MessageTelegramCommandProvider;
import com.example.beautybook.message.MessageProvider;
import com.example.beautybook.repository.user.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
@RequiredArgsConstructor
public class StartTelegramCommand implements Command<MessageTelegramCommandProvider> {
    private final UserRepository userRepository;

    @Override
    public String getKey() {
        return "/start";
    }

    @Override
    public void executeCommand(Object o, Object bot) {
        if (o instanceof Update update && bot instanceof TelegramBot telegramBot) {
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();
            username = username != null ? username : "User";
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .replyMarkup(createKeyboardMarkup(chatId))
                    .text(MessageProvider.getMessage("start", username))
                    .build();
            telegramBot.send(sendMessage);
        }
    }

    private ReplyKeyboardMarkup createKeyboardMarkup(Long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        if (userRepository.findByTelegramAccountChatId(chatId).isEmpty()) {
            row1.add("Get code");
            keyboard.add(row1);
            KeyboardRow row2 = new KeyboardRow();
            row2.add("Registration");
            row2.add("Support");
            keyboard.add(row2);
        } else {
            row1.add("Get code");
            row1.add("Support");
            keyboard.add(row1);
        }
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}
