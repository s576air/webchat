package com.s576air.webchat.service;

import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.domain.ChatroomCache;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatroomsCache {
    // key: ChatroomId
    private ConcurrentHashMap<Long, ChatroomCache> cache = new ConcurrentHashMap<>();

    public Optional<List<Chat>> addTextChatAndFlushChats(Long chatroomId, Long userId, String text) {
        ChatroomCache chatroomCache = cache.get(chatroomId);
        if (chatroomCache != null) {
            Chat chat = new Chat(0L, chatroomId, userId, "", text, LocalDateTime.now());

            return chatroomCache.addAndFlush(chat);
        }
        return Optional.empty();
    }
}
