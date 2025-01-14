package com.s576air.webchat.service;

import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.repository.ChatRepository;
import com.s576air.webchat.repository.ChatroomParticipantsRepository;
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
        Optional<List<Chat>> chats = chatroomsCache.addTextChatAndFlushChats(chatroomId, userId, text);
        if (chats.isPresent()) {
            for (Chat chat : chats.get()) {
                chatRepository.addChat(chat);
            }
        }
    }

    public Optional<List<Chat>> getChats(Long chatroomId, long id) {
        return chatRepository.getChats(chatroomId, id, 2);
    }

    public Optional<List<Chat>> getLastChats(Long chatroomId) {
        return chatRepository.getLastChats(chatroomId, 2);
    }
}
