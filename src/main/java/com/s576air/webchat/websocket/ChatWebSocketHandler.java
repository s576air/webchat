package com.s576air.webchat.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.domain.CustomUserDetails;
import com.s576air.webchat.dto.MessageRequestPayload;
import com.s576air.webchat.service.ChatService;
import com.s576air.webchat.service.ChatroomService;
import com.s576air.webchat.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ChatWebSocketHandler implements WebSocketHandler {
    private final ChatService chatService;
    private final ChatroomService chatroomService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ChatWebSocketHandler(ChatService chatService, ChatroomService chatroomService) {
        this.chatService = chatService;
        this.chatroomService = chatroomService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {}

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        Authentication authentication = (Authentication) session.getAttributes().get("user");

        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();

            if (message instanceof TextMessage) {
                Optional<String> returnMessage = handleTextMessage(session, (TextMessage) message, userId);
                if (returnMessage.isPresent()) {
                    session.sendMessage(new TextMessage(returnMessage.get()));
                }
            } else if (message instanceof BinaryMessage) {
                handleBinaryMessage(session, (BinaryMessage) message);
            }
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {}

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {}

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private Optional<String> handleTextMessage(WebSocketSession session, TextMessage message, Long userId) {
        String payload = message.getPayload();
        System.out.println("들어온 메시지: " + payload);

        MessageRequestPayload request;
        try {
            request = objectMapper.readValue(payload, MessageRequestPayload.class);
        } catch (JsonProcessingException e) {
            return Optional.of("{\"error\": \"요청 해석 실패\"}");
        }

        if (!chatroomService.containsUser(request.getChatroomId(), userId)) {
            return Optional.of("{\"error\": \"소속되지 않은 채팅방\"}");
        }

        if (request.getType().equals("send")) {
            chatService.saveTextMessage(request.getChatroomId(), userId, request.getText());
            return Optional.empty();
        } else if (request.getType().equals("load")) {
            LocalDateTime time;
            try {
                time = LocalDateTime.parse(request.getText());
            } catch (DateTimeException e) {
                return Optional.of("{\"error\": \"시간 해석 실패\"}");
            }
            Optional<List<Chat>> chats = chatService.getChats(request.getChatroomId(), Timestamp.valueOf(time));

            if (chats.isPresent()) {
                Optional<String> chatsJson = JsonUtil.toJson(chats.get());
                if (chatsJson.isPresent()) {
                    return chatsJson;
                }
            } else {
                return Optional.of("{\"error\": \"채팅 획득 실패\"}");
            }
        }
        return Optional.empty();
    }

    private void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        byte[] payload = message.getPayload().array();
        System.out.println("바이너리 메시지 길이(바이트): " + payload.length);
    }
}
