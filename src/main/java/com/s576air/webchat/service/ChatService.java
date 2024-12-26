package com.s576air.webchat.service;

import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.domain.ChatroomCache;
import com.s576air.webchat.repository.ChatRepository;
import com.s576air.webchat.repository.ChatroomParticipantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatroomParticipantsRepository chatroomParticipantsRepository;
    // key: chatroomId
    private ConcurrentHashMap<Long, ChatroomCache> cache = new ConcurrentHashMap<>();

    @Autowired
    public ChatService(ChatRepository chatRepository, ChatroomParticipantsRepository chatroomParticipantsRepository) {
        this.chatRepository = chatRepository;
        this.chatroomParticipantsRepository = chatroomParticipantsRepository;
    }

    public boolean saveTextMessage(Long chatroomId, Long userId, String text) {
        return chatRepository.addTextChat(chatroomId, userId, text);
    }

    public Optional<List<Chat>> getChats(Long chatroomId, Timestamp time) {
        return chatRepository.getChats(chatroomId, time, 2);
    }

    public Optional<List<Chat>> getLastChats(Long chatroomId) {
        return chatRepository.getLastChats(chatroomId, 2);
    }

    public void addUserSessionForChatrooms(Long userId, Long sessionId) {
        List<Long> chatroomIds = chatroomParticipantsRepository.findChatroomIdListByUserId(userId);
        for (Long chatroomId: chatroomIds) {
            ChatroomCache chatroomCache = cache.get(chatroomId);
            if (chatroomCache != null) {
                chatroomCache.addUserSession(userId, sessionId);
            }
        }
    }
}
