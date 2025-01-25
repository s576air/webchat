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

    public boolean addChat(Chat chat) {
        ChatroomCache chatroomCache = cache.get(chat.getChatroomId());
        if (chatroomCache == null) {
            return false;
        } else {
            chatroomCache.add(chat);
            return true;
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

    public Optional<List<Long>> getUserIds(Long chatroomId) {
        ChatroomCache chatroomCache = cache.get(chatroomId);
        if (chatroomCache == null) {
            return Optional.empty();
        } else {
            return Optional.of(chatroomCache.getUserIds());
        }
    }
}
