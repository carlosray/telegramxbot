package com.example.telegramxbot.bot.service.feature;

import java.util.Optional;

import com.example.telegramxbot.bot.service.DefaultAnswerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@RequiredArgsConstructor
public class SorryService {
    private final DefaultAnswerService defaultAnswerService;

    public boolean isNeedSorry(Message message, String botUsername) {
        return Optional.ofNullable(message.getReplyToMessage())
                .map(Message::getFrom)
                .filter(User::getIsBot)
                .filter(user -> user.getUserName().equals(botUsername))
                .flatMap(user -> Optional.of(message.getText())
                        .filter(StringUtils::isNotBlank)
                        .map(String::toLowerCase)
                        .filter(text -> StringUtils.containsAny(text,"извин", "извен", "проси проще")))
                .isPresent();
    }

    public String getSorryMessage() {
        return defaultAnswerService.getSorryMessage();
    }
}
