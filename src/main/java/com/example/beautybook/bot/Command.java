package com.example.beautybook.bot;

public interface Command<T> {
    String getKey();

    void executeCommand(Object o, Object bot);
}
