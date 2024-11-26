package com.s576air.webchat.service;

import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
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
}
