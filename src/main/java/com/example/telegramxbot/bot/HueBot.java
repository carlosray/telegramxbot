package com.example.telegramxbot.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;

import com.example.telegramxbot.bot.cache.ChatIdCache;
import com.example.telegramxbot.bot.properties.BotSettings;
import com.example.telegramxbot.bot.service.RandomService;
import com.example.telegramxbot.bot.service.feature.HueBotService;
import com.example.telegramxbot.bot.service.feature.QuoteService;
import com.example.telegramxbot.bot.service.feature.SorryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class HueBot extends TelegramLongPollingCommandBot {
    @Autowired
    private BotSettings settings;
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
    @Autowired
    private List<BotCommand> commands = new ArrayList<>();

    @PostConstruct
    void init() {
        BotCommand[] array = new BotCommand[commands.size()];
        registerAll(commands.toArray(array));
    }

    @Override
    public String getBotUsername() {
        return settings.getUsername();
    }

    @Override
    public String getBotToken() {
        return settings.getToken();
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (quoteService.isNeedToAnswerWithQuote(update.getMessage())) {
            processQuoteAnswer(update.getMessage());
        } else if (sorryService.isNeedSorry(update.getMessage(), getBotUsername())) {
            processSorryAnswer(update.getMessage());
        } else {
            processHueAnswer(update);
        }
    }

    private void processQuoteAnswer(Message message) {
        log.info("Processing quote answer");
        sendAnswer(message.getChatId().toString(), message.getMessageId(), quoteService.getQuote(), true);
    }

    private void processSorryAnswer(Message message) {
        log.info("Processing sorry answer");
        sendAnswer(message.getChatId().toString(), message.getMessageId(), sorryService.getSorryMessage(), false);
    }

    private void processHueAnswer(Update update) {
        Stream.of(update)
                .map(Update::getMessage)
                .filter(message -> !message.isCommand())
                .filter(message -> StringUtils.isNotBlank(message.getText()))
                .filter(message -> !StringUtils.isNumericSpace(message.getText()))
                .findAny()
                .filter(message -> randomService.isNeedAnswer(message.getChatId()))
                .ifPresent(message -> {
                    log.info("Processing hue answer");
                    String hueString = hueBotService.getHueString(message.getText());
                    if (StringUtils.isNotBlank(hueString)) {
                        sendAnswer(message.getChatId().toString(), message.getMessageId(), hueString, false);
                    }
                });
        chatIdCache.addToCache(update.getMessage().getChat());
    }

    public synchronized void sendImage(SendPhoto messageImage) {
        try {
            super.execute(messageImage);
            log.info("Sent image to chatId {}", messageImage.getChatId());
        } catch (Exception e) {
            log.error("Error image sending", e);
        }
    }

    public synchronized void sendAnswer(String chatId, @Nullable Integer replyToMessageId, String text, boolean markdown) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .replyToMessageId(replyToMessageId)
                .text(text)
                .build();
        if (replyToMessageId != null) {
            message.setReplyToMessageId(replyToMessageId);
        }
        if (markdown) {
            message.enableMarkdownV2(true);
            message.enableMarkdown(true);
            message.enableHtml(true);
        }
        try {
            super.execute(message);
            log.info("Sent answer to chatId {}", message.getChatId());
        } catch (Exception e) {
            log.error("Error answer sending", e);
        }
    }
}
