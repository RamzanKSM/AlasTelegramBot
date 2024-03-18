package com.kibiev.alastelegrambot.controller;

import com.kibiev.alastelegrambot.common.MessageController;
import com.kibiev.alastelegrambot.common.MessageMapping;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@MessageController
public class AdminController {

    @MessageMapping(mapping = "/admin")
    public void getAdminKeyboard(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            SendMessage message = new SendMessage();
            message.setChatId(chat_id);
            message.setText(message_text);
            try {
                execute(message); // Sending our message object to user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
