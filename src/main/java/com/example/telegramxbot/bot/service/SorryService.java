package com.example.telegramxbot.bot.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SorryService {
    @Autowired
    private RandomService randomService;

    private final String[] sorryMessages = {
            "Извините, плиз",
            "Сори гайз, май нейм из боба, донт сэй магоба",
            "Bağışlayın",
            "Прошу прощения, парни",
            "Sorry boys",
            "打擾一下",
            "Вибачте мене",
            "Къысфэгъэгъу"};

    public boolean isNeedSorry(Message message, String botUsername) {
        return Optional.of(message.getText())
                .filter(StringUtils::isNotBlank)
                .map(String::toLowerCase)
                .filter(text -> StringUtils.containsAny(text,"извин", "извен", "проси проще"))
                .flatMap(text -> Optional.ofNullable(message.getReplyToMessage())
                        .map(Message::getFrom)
                        .filter(User::getIsBot)
                        .filter(user -> user.getUserName().equals(botUsername)))
                .isPresent();
    }

    public String getSorryMessage() {
        return randomSorry();
    }

    private String randomSorry() {
        final AtomicInteger random = randomService.getRandomCount(0, sorryMessages.length - 1);
        return sorryMessages[random.get()];
    }
}
