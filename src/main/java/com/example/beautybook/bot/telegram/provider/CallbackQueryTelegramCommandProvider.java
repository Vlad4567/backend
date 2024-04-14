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
public class CallbackQueryTelegramCommandProvider implements CommandProvider<TelegramBot> {
    private final List<Command<CallbackQueryTelegramCommandProvider>> commands;

    @Override
    public String getKey() {
        return "callbackQuery";
    }

    @Override
    public Command<?> getCommand(Object o) {
        if (o instanceof Update update) {
            String data = update.getCallbackQuery().getData();
            return commands.stream()
                    .filter(command -> command.getKey().equals(data))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
