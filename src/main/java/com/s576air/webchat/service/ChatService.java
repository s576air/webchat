package com.s576air.webchat.service;

import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatroomsCache chatroomsCache;

    @Autowired
    public ChatService(ChatRepository chatRepository, ChatroomsCache chatroomsCache) {
        this.chatRepository = chatRepository;
        this.chatroomsCache = chatroomsCache;
    }

    public void saveTextMessage(Long chatroomId, Long userId, String text) {
        chatroomsCache.addChatroom(chatroomId);
        chatroomsCache.addTextChat(chatroomId, userId, text);
        chatRepository.addTextChat(chatroomId, userId, text);
    }

    public Optional<List<Chat>> getChats(Long chatroomId, long id) {
        return chatRepository.getChats(chatroomId, id, 2);
    }

    public Optional<List<Chat>> getLastChats(Long chatroomId) {
        return chatRepository.getLastChats(chatroomId, 2);
    }
}
