package com.example.telegramxbot.bot.command;

import com.example.telegramxbot.bot.service.feature.DaysOfWeekService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.Collections;

@Component
public class MemCommand extends BotCommand {
    private final DaysOfWeekService daysOfWeekService;

    public MemCommand(DaysOfWeekService daysOfWeekService) {
        super("mem", "могу дать мем по текущему дню недели");
        this.daysOfWeekService = daysOfWeekService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        daysOfWeekService.sendImage(Collections.singleton(chat.getId().toString()));
    }
}
