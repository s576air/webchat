package com.s576air.webchat.service;

import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.repository.ChatRepository;
import com.s576air.webchat.websocket.ChatWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UsersCache usersCache;
    private final ChatroomsCache chatroomsCache;

    @Autowired
    public ChatService(ChatRepository chatRepository, UsersCache usersCache, ChatroomsCache chatroomsCache) {
        this.chatRepository = chatRepository;
        this.usersCache = usersCache;
        this.chatroomsCache = chatroomsCache;
    }

    public void saveTextMessage(Long chatroomId, Long userId, String text) {
        Timestamp time = new Timestamp(new Date().getTime());
        Optional<Long> chatId = chatRepository.addTextChat(chatroomId, userId, text, time);

        if (chatId.isEmpty()) { return; }

        chatroomsCache.addChatroom(chatroomId);
        Chat chat = Chat.textChat(chatId.get(), chatroomId, userId, text, time);
        boolean result = chatroomsCache.addChat(chat);

        if (!result) { return; }

        Optional<List<Long>> userIds = chatroomsCache.getUserIds(chatroomId);

        if (userIds.isEmpty()) { return; }

        userIds.get()
            .forEach(chatroomUserId -> {
                usersCache.getSession(chatroomUserId).ifPresent(session -> {
                    ChatWebSocketHandler.sendTextChat(session, chat);
                });
            });
    }

    public Optional<List<Chat>> getChats(Long chatroomId, Long chatId) {
        chatroomsCache.getChats(chatroomId, chatId, 2);
        return chatRepository.getChats(chatroomId, chatId, 2);
    }

    public Optional<List<Chat>> getLastChats(Long chatroomId) {
        return chatRepository.getLastChats(chatroomId, 2);
    }
}
