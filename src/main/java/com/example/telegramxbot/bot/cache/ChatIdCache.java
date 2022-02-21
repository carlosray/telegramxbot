package com.example.telegramxbot.bot.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;

@Component
@Slf4j
public class ChatIdCache {

    public void addToCache(Chat chat) {
        ChatTypeCache.getCacheByChat(chat).ifPresentOrElse(
                type -> cacheChat(type, chat),
                () -> log.error("ChatTypeCache not found for {}", chat)
        );
    }

    public Set<Long> getChatIds() {
        return Arrays.stream(ChatTypeCache.values())
                .map(this::getChatIds)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<Long> getChatIds(ChatTypeCache type) {
        return type.getChatCache().keySet();
    }

    public Set<Chat> getChats() {
        return Arrays.stream(ChatTypeCache.values())
                .map(this::getChats)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<Chat> getChats(ChatTypeCache type) {
        return new HashSet<>(type.getChatCache().values());
    }

    private void cacheChat(ChatTypeCache type, Chat chat) {
        final boolean isAdded = type.addToCache(chat);
        if (isAdded) {
            log.info("Cached {} : {}", type.name(), chat.getId());
        }
    }
}
