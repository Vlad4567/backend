package com.example.beautybook.bot;

public interface BotCommandHandler<T> {
    void handleCommand(Object object, T bot);
}
