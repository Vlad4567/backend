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
public class ReplyMessageTelegramCommandProvider implements CommandProvider<TelegramBot> {
    private static final int INDEX_KEY = 0;
    private static final String DELIMITER = "-";
    private final List<Command<ReplyMessageTelegramCommandProvider>> commands;

    @Override
    public String getKey() {
        return "reply";
    }

    @Override
    public Command<?> getCommand(Object o) {
        if (o instanceof Update update) {
            String key = update.getMessage()
                    .getReplyToMessage()
                    .getText()
                    .split(DELIMITER)[INDEX_KEY];
            return commands.stream()
                    .filter(command -> command.getKey().equals(key))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
