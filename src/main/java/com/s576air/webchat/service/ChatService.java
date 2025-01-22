package com.s576air.webchat.service;

import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
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
        Timestamp time = new Timestamp(new Date().getTime());
        Optional<Long> chatId = chatRepository.addTextChat(chatroomId, userId, text, time);
        if (chatId.isPresent()) {
            chatroomsCache.addChatroom(chatroomId);
            Chat chat = Chat.textChat(chatId.get(), chatroomId, userId, text, time);
            chatroomsCache.addChat(chat);
            //
        }
    }

    public void sendMessage(Long chatroomId) {
        Optional<List<Long>> userIds = chatroomsCache.getUserIds(chatroomId);
        if (userIds != null) {
            //
        }
    }

    public Optional<List<Chat>> getChats(Long chatroomId, Long chatId) {
        chatroomsCache.getChats(chatroomId, chatId, 2);
        return chatRepository.getChats(chatroomId, chatId, 2);
    }

    public Optional<List<Chat>> getLastChats(Long chatroomId) {
        return chatRepository.getLastChats(chatroomId, 2);
    }
}
