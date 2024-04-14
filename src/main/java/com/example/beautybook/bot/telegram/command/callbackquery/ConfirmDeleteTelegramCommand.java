package com.example.beautybook.bot.telegram.command.callbackquery;

import com.example.beautybook.bot.Command;
import com.example.beautybook.bot.telegram.TelegramBot;
import com.example.beautybook.bot.telegram.provider.CallbackQueryTelegramCommandProvider;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.model.User;
import com.example.beautybook.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class ConfirmDeleteTelegramCommand implements Command<CallbackQueryTelegramCommandProvider> {
    private final UserRepository userRepository;

    @Override
    public String getKey() {
        return "#confirm:delete";
    }

    @Override
    public void executeCommand(Object o, Object bot) {
        if (o instanceof Update update && bot instanceof TelegramBot telegramBot) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            User user = userRepository.findByTelegramAccountChatId(chatId).orElseThrow(
                    () -> new EntityNotFoundException("Not found user by telegram chat id"));
            user.setTelegramAccount(null);
            String text;

            try {
                userRepository.save(user);
            } catch (Exception e) {
                text = "Failed to unlink account";
                telegramBot.send(SendMessage.builder().chatId(chatId).text(text).build());
            }
            text = "Account unlinked";
            telegramBot.send(SendMessage.builder().chatId(chatId).text(text).build());
        }
    }
}
