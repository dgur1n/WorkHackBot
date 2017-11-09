package ua.dgurin.workhackbot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Danylo_Hurin.
 */
public class LetraBot extends TelegramLongPollingBot {


    static Map<String, String> vocabulary = new HashMap<String, String>();

    public static void main(String[] args) {

        vocabulary.put("hello", "hello lovely world!");
        vocabulary.put("goodbye", "goodbye cruel world!");

        // Initialize Api Context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        // Register our bot
        try {
            botsApi.registerBot(new LetraBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            String s = vocabulary.get(message_text);
            SendMessage message = new SendMessage().setChatId(chat_id);
            if (s != null) {
                message.setText(s);


            } else {
                message.setText("Couldn't find such word");
            }
            send(message);
        }

    }

    private void send(SendMessage message) {
        try {
            sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public String getBotUsername() {
        return "LetraBot";
    }


    public String getBotToken() {
        return "480766557:AAFOnZkAdsONVoPAF057renTq5sPArPBeJo";
    }

}
