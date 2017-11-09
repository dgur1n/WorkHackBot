package ua.dgurin.workhackbot;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;


/**
 * @author Danylo_Hurin.
 */
public class WorkHackBot extends TelegramLongPollingBot {



    private static final String email = "your.email.here@gmail.com";

    private static final String hello = "Привет! Я Work Hack Бот. Я могу помочь с ответом на вопрос по трудовому праву." +
            " Я могу быть полезен тебе если в данный момент ты работаешь, ищешь работу или увольняешься." +
            " Просто выбери необходимый раздел из списка. Если ты не нашёл ответа на свой вопрос, обращайся пожалуйста сюда " + email + " ." +
            " Я постоянно совершенствуюсь и буду рад за обратную связь _______." +
            " Если ты готов, нажми «СТАРТ»";
    private static final String currentWork = "Мой вопрос связан с текущей работой";
    private static final String currentWorkCallback = "currentWorkCallback";
    private static final String firing = "Я в процессе увольнения";
    private static final String firingCallback = "firingCallback";

    private static final String looking = "Я ищу работу";
    private static final String lookingCallback = "Я ищу работу";
    private static final String lookingCouldAsk = "Какую информацию обо мне может запрашивать работодатель";
    private static final String lookingCouldAskCallback = "lookingCouldAskCallback";
    private static final String lookingCouldNotAsk = "Какую информацию не может запрашивать работодатель";
    private static final String lookingCouldNotAskCallback = "lookingCouldNotAsk";
    private static final String lookingProbation = "Какой испытательный срок положен по закону и как узнать об успешном его прохождении";
    private static final String lookingProbationCallback = "lookingProbation";
    private static final String lookingDeal = "Оформление по трудовому договору или заключение договора подряда (различия)";
    private static final String lookingDealCallback = "lookingDealCallback";

    private static final String askQuestion = "задати питання (in progress)";
    private static final String askQuestionCallback = "askQuestionCallback";

    private static final String helloCallback = "update_msg_text";
    private static final String commandStart = "/start";

    private static final String chooseOrYourQuestion = "Пожалуйста, выберите вопрос или задайте свой.";

    public static void main(String[] args) {
        // Initialize Api Context
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        // Register our bot
        try {
            botsApi.registerBot(new WorkHackBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            if (update.getMessage().getText().equals(commandStart)) {


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
            } else {

            }

        } else if (update.hasCallbackQuery()) {
            // Set variables
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals(helloCallback)) {
                String answer = "Пожалуйста, выберите категорию или задайте свой вопрос";
                SendMessage message = new SendMessage() // Create a message object object
                        .setChatId(chat_id)
                        .setText(answer);

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
                List<InlineKeyboardButton> rowInline1 = new ArrayList<InlineKeyboardButton>();
                List<InlineKeyboardButton> rowInline2 = new ArrayList<InlineKeyboardButton>();
                List<InlineKeyboardButton> rowInline3 = new ArrayList<InlineKeyboardButton>();
                List<InlineKeyboardButton> rowInline4 = new ArrayList<InlineKeyboardButton>();
                rowInline1.add(new InlineKeyboardButton().setText(currentWork).setCallbackData(currentWorkCallback));
                rowInline2.add(new InlineKeyboardButton().setText(firing).setCallbackData(firingCallback));
                rowInline3.add(new InlineKeyboardButton().setText(looking).setCallbackData(lookingCallback));
                rowInline4.add(new InlineKeyboardButton().setText(askQuestion).setCallbackData(askQuestionCallback));
                rowsInline.add(rowInline1);
                rowsInline.add(rowInline2);
                rowsInline.add(rowInline3);
                rowsInline.add(rowInline4);
                markupInline.setKeyboard(rowsInline);
                message.setReplyMarkup(markupInline);
                send(message);
            } else {
                if (call_data.equals(currentWorkCallback)) {
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(chat_id)
                            .setText(chooseOrYourQuestion);
                    send(message);

                } else if (call_data.equals(firingCallback)) {
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(chat_id)
                            .setText(chooseOrYourQuestion);
                    send(message);
                } else if (call_data.equals(lookingCallback)) {
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(chat_id)
                            .setText(chooseOrYourQuestion);

                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<List<InlineKeyboardButton>>();
                    List<InlineKeyboardButton> rowInline1 = new ArrayList<InlineKeyboardButton>();
                    List<InlineKeyboardButton> rowInline2 = new ArrayList<InlineKeyboardButton>();
                    List<InlineKeyboardButton> rowInline3 = new ArrayList<InlineKeyboardButton>();
                    List<InlineKeyboardButton> rowInline4 = new ArrayList<InlineKeyboardButton>();
                    List<InlineKeyboardButton> rowInline5 = new ArrayList<InlineKeyboardButton>();
                    rowInline1.add(new InlineKeyboardButton().setText(lookingCouldAsk).setCallbackData(lookingCouldAskCallback));
                    rowInline2.add(new InlineKeyboardButton().setText(lookingCouldNotAsk).setCallbackData(lookingCouldNotAskCallback));
                    rowInline3.add(new InlineKeyboardButton().setText(lookingProbation).setCallbackData(lookingProbationCallback));
                    rowInline4.add(new InlineKeyboardButton().setText(lookingDeal).setCallbackData(lookingDealCallback));
                    rowInline5.add(new InlineKeyboardButton().setText(askQuestion).setCallbackData(askQuestionCallback));
                    rowsInline.add(rowInline1);
                    rowsInline.add(rowInline2);
                    rowsInline.add(rowInline3);
                    rowsInline.add(rowInline4);
                    rowsInline.add(rowInline5);
                    markupInline.setKeyboard(rowsInline);
                    message.setReplyMarkup(markupInline);

                    send(message);
                } else if (call_data.equals(askQuestionCallback)) {
                    SendMessage message = new SendMessage() // Create a message object object
                            .setChatId(chat_id)
                            .setText("Please enter your question");

                    send(message);
                } else {
                    System.out.println("Could not find callback");
                }
            }
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
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "WorkHackBot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "473289720:AAGEOWPMFIAMQi3qpgLcip117o_JDpaPKcE";
    }
}
