package com.kibiev.alastelegrambot.config;

import com.kibiev.alastelegrambot.common.MessageDispatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

@Configuration
@RequiredArgsConstructor
public class BotHandler extends BaseBotConfig {

    private final MessageDispatcher messageDispatcher;

    @Override
    public void onUpdateReceived(Update update) {
        messageDispatcher.delegateUpdate(update);
    }
}
