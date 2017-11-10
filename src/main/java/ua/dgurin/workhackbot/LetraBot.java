package ua.dgurin.workhackbot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Danylo_Hurin.
 */
public class LetraBot extends TelegramLongPollingBot {

    private static final String email = "your.email.here@gmail.com";

    private static final String hello = "Привет! Я Work Hack Бот. Я могу помочь с ответом на вопрос по трудовому праву." +
            " Я могу быть полезен тебе если в данный момент ты работаешь, ищешь работу или увольняешься." +
            " Просто выбери необходимый раздел из списка. Если ты не нашёл ответа на свой вопрос, обращайся пожалуйста сюда " + email + " ." +
            " Я постоянно совершенствуюсь и буду рад за обратную связь _______." +
            " Если ты готов, нажми «СТАРТ»";

    private static final String commandStart = "/start";
    private static final String commandChooseVocabulary = "/choose";
    private static final String helloCallback = "update_msg_text";

    static String catDeal = "Dogovirne";
    static String catCommon = "Zagal'ne";
    static String catCriminal = "Criminal";
    static String catAll = "All";

    static Map<String, WordDefinition> vocabulary = new HashMap<String, WordDefinition>();
    static Map<String, Map<String, WordDefinition>> vocabulary2 = new HashMap<String, Map<String, WordDefinition>>();

    static String currentCategory = "";

    public static void main(String[] args) {

        vocabulary = fillVocabulary("/code/WorkHackBot/vocabularyCommon.csv");
        vocabulary2 = fillVocabulary2();

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

    private static Map<String, Map<String, WordDefinition>> fillVocabulary2() {
        Map<String, Map<String, WordDefinition>> vocabulary = new HashMap<String, Map<String, WordDefinition>>();
        String csvCriminal = "/code/WorkHackBot/vocabularyCriminal.csv";
        String csvDeal = "/code/WorkHackBot/vocabularyDeal.csv";
        String csvCommon = "/code/WorkHackBot/vocabularyCommon.csv";

        Map<String, WordDefinition> criminal = fillVocabulary(csvCriminal);
        vocabulary.put(catCriminal, criminal);

        Map<String, WordDefinition> common = fillVocabulary(csvCommon);
        vocabulary.put(catCommon, common);

        Map<String, WordDefinition> deal = fillVocabulary(csvDeal);
        vocabulary.put(catDeal, deal);

        return vocabulary;

    }

    private static Map<String, WordDefinition> fillVocabulary(String csvFile) {

        Map<String, WordDefinition> vocabulary = new HashMap<String, WordDefinition>();
        String line = "";
        String cvsSplitBy = "\\*";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // use comma as separator
                System.out.println("-> " + line);
                String[] definition = line.split(cvsSplitBy);
                for (String we : definition) System.out.println(" ---> " + we);
                WordDefinition d = new WordDefinition();
                d.setWord(definition[0].trim());
                d.setDefinition(definition[1]);
                d.setTranslation(definition[2]);
                if (definition.length >= 4) {
                    d.setSample(definition[3]);
                } else {
                    d.setSample("");
                }
                if (definition.length >= 5) {
                    d.setOther(definition[4]);
                } else {
                    d.setOther("");
                }
                vocabulary.put(d.getWord(), d);
                System.out.println(d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vocabulary;
    }

    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText().trim();
            long chat_id = update.getMessage().getChatId();

            if (message_text.equals(commandStart)) {
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText(hello);
                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
                List<InlineKeyboardButton> rowInline = new ArrayList<InlineKeyboardButton>();
                rowInline.add(new InlineKeyboardButton().setText("START").setCallbackData(helloCallback));
                // Set the keyboard to the markup
                rowsInline.add(rowInline);
                // Add it to the message
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                send(message);
            } else if (message_text.equals(commandChooseVocabulary)) {
                sendChooseVocabulary(chat_id);
            } else {
                Map<String, WordDefinition> currentVocabulary = vocabulary2.get(currentCategory);

                if (currentVocabulary == null) {
                    boolean wasSent = false;
                    for (Map<String, WordDefinition> v : vocabulary2.values()) {
                        if (wasSent) {
                            SendMessage message = new SendMessage().setChatId(chat_id);
                            message.setText("-------------------");
                            send(message);
                        }
                        boolean sent = sendTransation(message_text, chat_id, v, false);
                        wasSent = wasSent || sent;
                    }
                    if (!wasSent) {
                        SendMessage message = new SendMessage().setChatId(chat_id);
                        message.setText("Couldn't find word '" + message_text);
                        send(message);
                    }
                } else {
                    sendTransation(message_text, chat_id, currentVocabulary, true);
                }

            }
        }
        else if (update.hasCallbackQuery()) {
            // Set variables
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals(helloCallback)) {
                sendChooseVocabulary(chat_id);
            } else if (call_data.equals(catDeal)) {
                currentCategory = catDeal;
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("Searching in " + catDeal);
                send(message);
            } else if (call_data.equals(catCriminal)) {
                currentCategory = catCriminal;
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("Searching in " + catCriminal);
                send(message);
            } else if (call_data.equals(catCommon)) {
                currentCategory = catCommon;
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("Searching in " + catCommon);
                send(message);
            } else if (call_data.equals(catAll)) {
                currentCategory = "";
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText("Searching in everywhere...");
                send(message);
            }
        }
    }

    private void sendChooseVocabulary(long chat_id) {
        String answer = "Пожалуйста, выберите категорию";
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chat_id)
                .setText(answer);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> rowInline2 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> rowInline3 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> rowInline4 = new ArrayList<InlineKeyboardButton>();
        rowInline1.add(new InlineKeyboardButton().setText(catDeal).setCallbackData(catDeal));
        rowInline2.add(new InlineKeyboardButton().setText(catCriminal).setCallbackData(catCriminal));
        rowInline3.add(new InlineKeyboardButton().setText(catCommon).setCallbackData(catCommon));
        rowInline4.add(new InlineKeyboardButton().setText(catAll).setCallbackData(catAll));
        rowsInline.add(rowInline1);
        rowsInline.add(rowInline2);
        rowsInline.add(rowInline3);
        rowsInline.add(rowInline4);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        send(message);
    }

    private boolean sendTransation(String message_text, long chat_id, Map<String, WordDefinition> currentVocabulary, boolean sendNotFound) {
        boolean wasSent = false;
        WordDefinition s = currentVocabulary.get(message_text);
        SendMessage message = new SendMessage().setChatId(chat_id);
        if (s != null) {
            StringBuilder b = new StringBuilder();
            b.append("Слово или выражение: ").append(s.getWord()).append("\n");
            message.setText(b.toString()); send(message); b.setLength(0);
            b.append("Определение: ").append(s.getDefinition()).append("\n");
            message.setText(b.toString()); send(message); b.setLength(0);
            b.append("Перевод: ").append(s.getTranslation()).append("\n");
            message.setText(b.toString()); send(message); b.setLength(0);
            b.append("Пример использования: ").append(s.getSample()).append("\n");
            message.setText(b.toString()); send(message); b.setLength(0);

//                b.append("Другая информация: ").append(s.getOther()).append("\n");
//                message.setText(b.toString()); send(message);
            wasSent = true;
        } else {
            if (sendNotFound) {
                message.setText("Couldn't find word '" + message_text + "' in vocabulary");
                send(message);
            }

        }
        return wasSent;
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
