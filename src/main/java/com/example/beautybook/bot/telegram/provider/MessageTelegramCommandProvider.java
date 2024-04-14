package com.example.beautybook.bot.telegram.provider;

import com.example.beautybook.bot.Command;
import com.example.beautybook.bot.CommandProvider;
import com.example.beautybook.bot.telegram.TelegramBot;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class MessageTelegramCommandProvider implements CommandProvider<TelegramBot> {
    private final List<Command<MessageTelegramCommandProvider>> commands;

    @Override
    public String getKey() {
        return "message";
    }

    @Override
    public Command<?> getCommand(Object o) {
        if (o instanceof Update update) {
            String key = update.getMessage().getText();
            return commands.stream()
                    .filter(command -> command.getKey().equals(key))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
