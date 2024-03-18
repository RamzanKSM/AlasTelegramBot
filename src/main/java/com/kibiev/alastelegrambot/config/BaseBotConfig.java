package com.kibiev.alastelegrambot.config;

import com.kibiev.alastelegrambot.common.MessageDispatcher;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
@RequiredArgsConstructor
public class BotConfig extends TelegramLongPollingBot {
    @Value("${telegram.token}")
    private final String token;
    @Value("${telegram.name}")
    private final String name;

    private final MessageDispatcher messageDispatcher;

    @Override
    public void onUpdateReceived(Update update) {
        messageDispatcher.delegateUpdate(update);
    }

    @Override
    public void execute(BotApiMethod<?> method) {

    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotUsername() {
        return name;
    }
}
