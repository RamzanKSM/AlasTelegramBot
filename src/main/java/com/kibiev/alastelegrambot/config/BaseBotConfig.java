package com.kibiev.alastelegrambot.config;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;


@NoArgsConstructor
public abstract class BaseBotConfig extends TelegramLongPollingBot {
    @Value("${telegram.token}")
    private String token;
    @Value("${telegram.name}")
    private String name;

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return name;
    }
}
