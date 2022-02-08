package com.example.telegramxbot.bot.cache;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashSet;

@Component
@Getter
@Slf4j
public class ChatIdCache {
    private final HashSet<String> groupChatIds = new HashSet<>();
    private final HashSet<String> userChatIds = new HashSet<>();

    public void addToCache(Message message) {
        final Chat chat = message.getChat();
        if (chat.isGroupChat() || chat.isSuperGroupChat()) {
            addTo(groupChatIds, chat);
            log.info("Cached group chat {}", chat.getId());
        }
        else if (chat.isUserChat()) {
            addTo(userChatIds, chat);
            log.info("Cached user chat {}", chat.getId());
        }
    }

    private void addTo(HashSet<String> cache, Chat chat) {
        cache.add(chat.getId().toString());
    }
}
