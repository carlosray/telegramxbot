package com.example.telegramxbot.bot.service;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.telegramxbot.bot.properties.BotSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomService {
    private final ConcurrentMap<Long, AtomicInteger> countMap = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private final BotSettings settings;

    private AtomicInteger getRandomCount() {
        return this.getRandomCount(
                settings.getMessages().getCount().getMin(),
                settings.getMessages().getCount().getMax()
        );
    }

    public AtomicInteger getRandomCount(int min, int max) {
        int num = random.nextInt(max - min) + min;
        return new AtomicInteger(num);
    }

    public boolean isNeedAnswer(Long chatId) {
        if (!countMap.containsKey(chatId)) {
            countMap.put(chatId, getRandomCount());
        }
        if (countMap.get(chatId).decrementAndGet() == 0) {
            countMap.put(chatId, getRandomCount());
            return true;
        } else {
            return false;
        }
    }


}
