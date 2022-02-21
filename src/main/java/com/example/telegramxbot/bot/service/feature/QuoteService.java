package com.example.telegramxbot.bot.service.feature;

import java.util.Objects;

import com.example.telegramxbot.bot.entity.Quote;
import com.example.telegramxbot.bot.properties.BotSettings;
import com.example.telegramxbot.bot.repo.QuoteRepository;
import com.example.telegramxbot.bot.service.DefaultAnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuoteService {
    private final BotSettings settings;
    private final QuoteRepository repository;
    private final DefaultAnswerService defaultAnswerService;


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
        return repository.findRandom()
                .map(Quote::toString)
                .orElse(defaultAnswerService.getDefaultQuote());
    }
}
