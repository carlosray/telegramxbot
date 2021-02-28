package com.example.telegramxbot.bot.service;

import com.example.telegramxbot.bot.HueBot;
import com.example.telegramxbot.bot.cache.ChatIdCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DaysOfWeekService {
    private final HueBot hueBot;
    @Value("${bot.huebot.days-of-week.zone:Europe/Moscow}")
    private String zone;
    private final ChatIdCache chatIdCache;
    private final GoogleImagesService imagesService;
    private static final String FORMAT = ".jpg";

    @Scheduled(cron = "${bot.huebot.days-of-week.cron}", zone = "${bot.huebot.days-of-week.zone:Europe/Moscow}")
    public void sendImage() {
        chatIdCache.getGroupChatIds().forEach(chatId -> {
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
            hueBot.execute(messageImage);
        } catch (TelegramApiException e) {
            log.error("Ошибка отправки сообщения", e);
        }
    }

    private DayOfWeek getDayOfWeek() {
        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of(zone));
        return dateTime.getDayOfWeek();
    }
}
