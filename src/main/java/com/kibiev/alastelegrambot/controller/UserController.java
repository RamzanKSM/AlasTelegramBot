package com.kibiev.alastelegrambot.controller;

import com.kibiev.alastelegrambot.common.MessageController;
import com.kibiev.alastelegrambot.common.MessageMapping;
import com.kibiev.alastelegrambot.config.BotHandler;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@MessageController
@RequiredArgsConstructor
public class UserController {
    private final BotHandler botHandler;
    @MessageMapping("/user")
    public void getUserKeyboard(Update update) {

    }
}
