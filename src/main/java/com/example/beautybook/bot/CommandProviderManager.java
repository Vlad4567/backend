package com.example.beautybook.bot;

public interface CommandProviderManager<T> {
    CommandProvider<T> getCommandProvider(String key);
}
