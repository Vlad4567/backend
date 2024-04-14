package com.example.beautybook.bot;

public interface CommandProvider<T> {
    String getKey();

    Command<?> getCommand(Object o);
}
