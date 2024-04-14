package com.example.beautybook.config;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public ScheduledExecutorService executor() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
