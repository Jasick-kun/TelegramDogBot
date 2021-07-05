package kz.jasick.dogbot;

import kz.jasick.dogbot.Controllers.GetBreedsListController;
import kz.jasick.dogbot.Controllers.GetPhotoController;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class DogBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "DogsPictureBot";
    }

    @Override
    public String getBotToken() {
        return "1808091857:AAFQl8549XoqKGCvnRAZG0Y7WaXCWsTTXSc";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.getMessage().hasText()){
            String messageIn=update.getMessage().getText();
            String firstWord=messageIn.contains(" ") ? messageIn.split(" ")[0] : messageIn;
            if (firstWord.equals("Картинка")||firstWord.equals("картинка")){
                try {
                    execute(GetPhotoController.getPhoto(update, messageIn));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else if(update.getMessage().getText().equals("Список пород")||update.getMessage().getText().equals("список пород")){
                try {
                    execute(GetBreedsListController.getBreedsList(update));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else{

            SendMessage message = SendMessage.builder()
                    .chatId(update.getMessage().getChatId().toString())
                    .text("Команды: \nСписок пород\nКартинка \"название породы\"")
                    .build();
            setButtons(message);
            try {
                execute(message);

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }}
            Database.checkUser(update);

    }}

    public synchronized void setButtons(SendMessage sendMessage) {
        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Команды"));
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add(new KeyboardButton("Список пород"));
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

}
