package com.example.telegramxbot.bot.service;

import java.util.Optional;
import java.util.Set;

import com.example.telegramxbot.bot.entity.DefaultAnswer;
import com.example.telegramxbot.bot.repo.DefaultAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultAnswerService {
    private DefaultAnswerRepository repository;
    private RandomService randomService;

    public String getSorryMessage() {
        return getRandomByType(DefaultAnswer.AnswerType.SORRY).orElse("Извините меня");
    }

    public String getSmileMessage() {
        return getRandomByType(DefaultAnswer.AnswerType.SMILE).orElse("");
    }

    public String getWildMessage() {
        return getRandomByType(DefaultAnswer.AnswerType.WILD).orElse("Не распарсил сообщение..");
    }

    public String getDefaultQuote() {
        return "Не смог загрузить цитаты..";
    }

    private Optional<String> getRandomByType(DefaultAnswer.AnswerType type) {
        final Set<DefaultAnswer> allByType = repository.findAllByType(type);
        return randomService.getRandomSetElement(allByType)
                .map(DefaultAnswer::getText);
    }
}
