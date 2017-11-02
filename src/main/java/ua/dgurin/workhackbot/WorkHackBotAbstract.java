package ua.dgurin.workhackbot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;


/**
 * @author Danylo_Hurin.
 */
public abstract class WorkHackBotAbstract extends TelegramLongPollingBot {

//    public abstract TelegramLongPollingBot instance();


    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "WorkHackBot";
    }


    public String getBotToken() {
        // Return bot token from BotFather
        return "";
    }
}
