package com.s576air.webchat.service;

import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.domain.ChatroomCache;
import com.s576air.webchat.repository.ChatroomParticipantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatroomsCache {
    private final ChatroomParticipantsRepository chatroomParticipantsRepository;
    // key: ChatroomId
    private ConcurrentHashMap<Long, ChatroomCache> cache = new ConcurrentHashMap<>();

    @Autowired
    public ChatroomsCache(ChatroomParticipantsRepository chatroomParticipantsRepository) {
        this.chatroomParticipantsRepository = chatroomParticipantsRepository;
    }

    public void addChatroom(Long chatroomId) {
        if (!cache.containsKey(chatroomId)) {
            List<Long> userIds = chatroomParticipantsRepository.findUserIdListByChatroomId(chatroomId);
            cache.put(chatroomId, new ChatroomCache(userIds));
        }
    }

    public void removeChatroomIfIdle(Long chatroomId) {
        ChatroomCache chatroomCache = cache.get(chatroomId);
        if (chatroomCache != null && chatroomCache.isIdle()) {
            cache.remove(chatroomId);
        }
    }

    public void addTextChat(Long chatroomId, Long userId, String text, Long id, Timestamp time) {
        ChatroomCache chatroomCache = cache.get(chatroomId);
        if (chatroomCache != null) {
            Chat chat = new Chat(id, chatroomId, userId, "", text, time);

            chatroomCache.add(chat);
        }
    }

    public Optional<List<Chat>> getChats(Long chatroomId, Long chatId, int limit) {
        ChatroomCache chatroomCache = cache.get(chatroomId);
        if (chatroomCache == null) {
            return Optional.empty();
        } else {
            return chatroomCache.getChats(chatId, limit);
        }
    }
}
