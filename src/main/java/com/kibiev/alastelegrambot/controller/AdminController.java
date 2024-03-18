package com.kibiev.alastelegrambot.controller;

import com.kibiev.alastelegrambot.common.MessageController;
import com.kibiev.alastelegrambot.common.MessageMapping;
import com.kibiev.alastelegrambot.config.BotHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@MessageController
@RequiredArgsConstructor
public class AdminController {

    @Lazy
    private final BotHandler botHandler;

    @MessageMapping("/admin")
    public void getAdminKeyboard(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            SendMessage message = new SendMessage();
            message.setChatId(chat_id);
            message.setText(message_text);
            try {
                botHandler.execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    @MessageMapping("/adminStart")
    public void startAdminKeyboard(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (update.getMessage().getFrom().getUserName().equals("pilot_dev")) {
                SendMessage message = new SendMessage();
                message.setChatId(update.getMessage().getChatId());
                message.setText("U r admin here");
                try {
                    botHandler.execute(message);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
