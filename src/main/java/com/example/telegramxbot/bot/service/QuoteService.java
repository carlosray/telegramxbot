package com.example.telegramxbot.bot.service;

import com.example.telegramxbot.bot.model.Quote;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

@Service
public class QuoteService {
    @Value("${bot.huebot.username}")
    private String username;
    private final Quote[] quotes;
    private final Random random = new Random();

    public QuoteService() throws IOException {
        ClassPathResource resource = new ClassPathResource("quotes.json");
        ObjectMapper objectMapper = new ObjectMapper();
        quotes = objectMapper.readValue(resource.getInputStream(), new TypeReference<Quote[]>(){});
    }

    public boolean isNeedToAnswerWithQuote(Message message) {
        if (Objects.isNull(message.getEntities())) {
            return false;
        }
        return message.getEntities().stream()
                .filter(messageEntity -> StringUtils.isNotBlank(messageEntity.getType()))
                .filter(messageEntity -> messageEntity.getType().equalsIgnoreCase("mention"))
                .filter(messageEntity -> StringUtils.isNotBlank(messageEntity.getText()))
                .anyMatch(messageEntity -> messageEntity.getText().equalsIgnoreCase("@".concat(username)));
    }

    public String getQuote() {
        int low = 0;
        int high = quotes.length-1;
        int result = random.nextInt(high-low) + low;
        return quotes[result].toString();
    }
}
