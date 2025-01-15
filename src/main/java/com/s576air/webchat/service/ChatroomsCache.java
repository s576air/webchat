package com.s576air.webchat.service;

import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.domain.ChatroomCache;
import com.s576air.webchat.repository.ChatroomParticipantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
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

    public void addTextChat(Long chatroomId, Long userId, String text) {
        ChatroomCache chatroomCache = cache.get(chatroomId);
        if (chatroomCache != null) {
            Chat chat = new Chat(0L, chatroomId, userId, "", text, LocalDateTime.now());

            chatroomCache.add(chat);
        }
    }
}
