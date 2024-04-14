package com.example.beautybook.bot.telegram.impl;

import com.example.beautybook.bot.CommandProvider;
import com.example.beautybook.bot.CommandProviderManager;
import com.example.beautybook.bot.telegram.TelegramBot;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramBotCommandProviderManagerImpl implements CommandProviderManager<TelegramBot> {
    private final List<CommandProvider<TelegramBot>> providers;

    @Override
    public CommandProvider<TelegramBot> getCommandProvider(String key) {
        return providers.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found provider by key " + key));
    }
}
