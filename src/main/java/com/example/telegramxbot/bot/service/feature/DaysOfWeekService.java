package com.example.telegramxbot.bot.service.feature;

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

import com.example.telegramxbot.bot.HueBot;
import com.example.telegramxbot.bot.cache.ChatIdCache;
import com.example.telegramxbot.bot.service.integration.GoogleImagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class DaysOfWeekService {
    @Autowired
    private ApplicationContext applicationContext;
    @Value("#{botSettings.birthday.zone}")
    private String zone;
    private final ChatIdCache chatIdCache;
    private final GoogleImagesService imagesService;
    private static final String FORMAT = ".jpg";

    @Scheduled(cron = "#{botSettings.daysOfWeek.cron}", zone = "#{botSettings.daysOfWeek.zone}")
    public void sendImage() {
        this.sendImage(chatIdCache.getGroupChatIds());
    }

    public void sendImage(Set<String> chatIds) {
        chatIds.forEach(chatId -> {
            final DayOfWeek dayOfWeek = getDayOfWeek();
            try (InputStream resourceInputStream = getResourceInputStream(dayOfWeek)) {
                sendImageToTelegram(chatId, resourceInputStream, imageFileName(dayOfWeek));
            } catch (IOException e) {
                log.error("Ошибка получения картинки", e);
            }
        });
    }

    private InputStream getResourceInputStream(DayOfWeek dayOfWeek) throws IOException {
        try {
            return imagesService.getImageInputStream(dayOfWeek);
        } catch (Exception ex) {
            return getClassPathResource(dayOfWeek);
        }
    }

    private InputStream getClassPathResource(DayOfWeek dayOfWeek) throws IOException {
        ClassPathResource resource = new ClassPathResource("daysOfWeek/".concat(imageFileName(dayOfWeek)));
        return resource.getInputStream();
    }

    private String imageFileName(DayOfWeek dayOfWeek) {
        return dayOfWeek.name().toLowerCase().concat(FORMAT);
    }

    private synchronized void sendImageToTelegram(String chatId, InputStream resourceInputStream, String fileName) {
        SendPhoto messageImage = SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(resourceInputStream, fileName))
                .build();
        try {
            final HueBot hueBot = applicationContext.getBean(HueBot.class);
            hueBot.execute(messageImage);
        } catch (Exception e) {
            log.error("Ошибка отправки сообщения", e);
        }
    }

    private DayOfWeek getDayOfWeek() {
        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of(zone));
        return dateTime.getDayOfWeek();
    }
}
