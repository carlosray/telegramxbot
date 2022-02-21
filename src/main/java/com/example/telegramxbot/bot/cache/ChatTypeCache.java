package com.example.telegramxbot.bot.cache;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Chat;

@RequiredArgsConstructor
public enum ChatTypeCache {
    GROUP_CHAT(chat -> chat.isGroupChat() || chat.isSuperGroupChat()),
    USER_CHAT(Chat::isUserChat);

    @Getter(AccessLevel.PACKAGE)
    private final Map<Long, Chat> chatCache = new ConcurrentHashMap<>();
    private final Predicate<Chat> isSuitableForCaching;

    /**
     * Поместить чат в кеш
     *
     * @return {@code true} если добавили в чат, {@code false} если уже был в кеше
     */
    boolean addToCache(Chat chat) {
        return chatCache.putIfAbsent(chat.getId(), chat) == null;
    }

    static Optional<ChatTypeCache> getCacheByChat(Chat chat) {
        return Arrays.stream(values())
                .filter(type -> type.isSuitableForCaching.test(chat))
                .findFirst();
    }
}
