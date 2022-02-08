package com.example.telegramxbot.bot.service.feature;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.example.telegramxbot.bot.model.Quote;
import com.example.telegramxbot.bot.properties.BotSettings;
import com.example.telegramxbot.bot.service.RandomService;
import com.example.telegramxbot.bot.service.utils.ResourceUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@RequiredArgsConstructor
public class QuoteService {
    private final List<Quote> quotes = ResourceUtils.mapFromResource("quotes.json");
    private final RandomService randomService;
    private final BotSettings settings;

    public boolean isNeedToAnswerWithQuote(Message message) {
        if (Objects.isNull(message.getEntities())) {
            return false;
        }
        return message.getEntities().stream()
                .filter(messageEntity -> StringUtils.isNotBlank(messageEntity.getType()))
                .filter(messageEntity -> messageEntity.getType().equalsIgnoreCase("mention"))
                .filter(messageEntity -> StringUtils.isNotBlank(messageEntity.getText()))
                .anyMatch(messageEntity -> messageEntity.getText().equalsIgnoreCase("@".concat(settings.getUsername())));
    }

    public String getQuote() {
        if (quotes != null && quotes.size() > 1) {
            int result = randomService.getRandomCount(0, quotes.size() - 1).get();
            return quotes.get(result).toString();
        } else {
            return "Чет я приболел, не смог загрузить цитаты..";
        }
    }
}
