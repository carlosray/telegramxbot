package com.example.telegramxbot.bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RandomService {
    private ConcurrentMap<Long, AtomicInteger> countMap = new ConcurrentHashMap<>();
    @Value("${bot.huebot.messages.count.min}")
    private int min;
    @Value("${bot.huebot.messages.count.max}")
    private int max;

    public AtomicInteger getRandomCount() {
        return this.getRandomCount(this.min, this.max);
    }

    public AtomicInteger getRandomCount(double min, double max) {
        double random = Math.random() * (max - min) + min;
        return new AtomicInteger((int) Math.round(random));
    }

    public boolean isNeedAnswer(Long chatId) {
        if (!countMap.containsKey(chatId)) {
            countMap.put(chatId, getRandomCount());
        }
        if (countMap.get(chatId).decrementAndGet() == 0) {
            countMap.put(chatId, getRandomCount());
            return true;
        }
        else {
            return false;
        }
    }



}
