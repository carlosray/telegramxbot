package com.example.telegramxbot.bot;

import com.example.telegramxbot.bot.cache.ChatIdCache;
import com.example.telegramxbot.bot.service.HueBotService;
import com.example.telegramxbot.bot.service.QuoteService;
import com.example.telegramxbot.bot.service.RandomService;
import com.example.telegramxbot.bot.service.SorryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.stream.Stream;

@Component
public class HueBot extends TelegramLongPollingBot {
    @Value("${bot.huebot.username}")
    private String username;
    @Value("${bot.huebot.token}")
    private String token;

    @Autowired
    private HueBotService hueBotService;
    @Autowired
    private RandomService randomService;
    @Autowired
    private QuoteService quoteService;
    @Autowired
    private ChatIdCache chatIdCache;
    @Autowired
    private SorryService sorryService;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (quoteService.isNeedToAnswerWithQuote(update.getMessage())) {
            processQuoteAnswer(update.getMessage());
        }
        else if (sorryService.isNeedSorry(update.getMessage(), username)) {
            processSorryAnswer(update.getMessage());
        }
        else {
            processHueAnswer(update);
        }
    }

    private void processQuoteAnswer(Message message) {
        sendAnswer(message.getChatId().toString(), message.getMessageId(), quoteService.getQuote(), true);
    }

    private void processSorryAnswer(Message message) {
        sendAnswer(message.getChatId().toString(), message.getMessageId(), sorryService.getSorryMessage(), false);
    }

    private void processHueAnswer(Update update) {
        Stream.of(update)
                .map(Update::getMessage)
                .filter(message -> !message.isCommand())
                .filter(message -> StringUtils.isNotBlank(message.getText()))
                .filter(message -> !StringUtils.isNumericSpace(message.getText()))
                .findAny()
                .ifPresent(message -> {
                    String hueString = hueBotService.getHueString(message.getText());
                    if (StringUtils.isNotBlank(hueString) && randomService.isNeedAnswer(message.getChatId())) {
                        sendAnswer(message.getChatId().toString(), message.getMessageId(), hueString, false);
                    }
                });
        chatIdCache.addToCache(update.getMessage());
    }

    private synchronized void sendAnswer(String chatId, Integer messageId, String text, boolean markdown) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .replyToMessageId(messageId)
                .text(text)
                .build();
        if (markdown) {
            message.enableMarkdownV2(true);
            message.enableMarkdown(true);
            message.enableHtml(true);
        }
        try {
            super.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
