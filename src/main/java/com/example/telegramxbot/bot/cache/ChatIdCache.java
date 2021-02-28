package com.example.telegramxbot.bot.cache;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashSet;

@Component
@Getter
public class ChatIdCache {
    private final HashSet<String> groupChatIds = new HashSet<>();
    private final HashSet<String> userChatIds = new HashSet<>();

    public void addToCache(Message message) {
        final Chat chat = message.getChat();
        if (chat.isGroupChat() || chat.isSuperGroupChat()) {
            addTo(groupChatIds, chat);
        }
        else if (chat.isUserChat()) {
            addTo(userChatIds, chat);
        }
    }

    private void addTo(HashSet<String> cache, Chat chat) {
        cache.add(chat.getId().toString());
    }
}
