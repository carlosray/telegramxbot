package com.example.telegramxbot.bot.service.feature;

import com.example.telegramxbot.bot.HueBot;
import com.example.telegramxbot.bot.cache.ChatIdCache;
import com.example.telegramxbot.bot.service.integration.BalabobaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BirthdayService {
    @Autowired
    private ApplicationContext applicationContext;
    private final ChatIdCache chatIdCache;
    private final BalabobaService balabobaService;

    @Scheduled(cron = "#{botSettings.birthday.cron}", zone = "#{botSettings.birthday.zone}")
    public void sendBirthday() {
        final String generated = balabobaService.generate("С днем рождения!");
        final HueBot bot = applicationContext.getBean(HueBot.class);
//        bot.sendAnswer(chatIdCache.getGroupChatIds(), null, generated, false);
    }

}
