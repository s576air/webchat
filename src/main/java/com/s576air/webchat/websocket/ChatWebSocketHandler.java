package com.s576air.webchat.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s576air.webchat.domain.CustomUserDetails;
import com.s576air.webchat.dto.MessageRequestPayload;
import com.s576air.webchat.service.ChatService;
import com.s576air.webchat.service.ChatroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Map;

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
        if (message instanceof TextMessage) {
            handleTextMessage((TextMessage) message);
        } else if (message instanceof BinaryMessage) {
            handleBinaryMessage(session, (BinaryMessage) message);
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

    private void handleTextMessage(TextMessage message) {
        String payload = message.getPayload();
        System.out.println("텍스트 메시지: " + payload);

        Long userId;
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            userId = userDetails.getId();
        } catch (Exception e) {
            return;
        }

        MessageRequestPayload request;
        try {
            request = objectMapper.readValue(payload, MessageRequestPayload.class);
        } catch (JsonProcessingException e) {
            return;
        }

        if (!chatroomService.containsUser(request.getChatroomId(), userId)) {
            return;
        }

        if (request.getType().equals("send")) {
            chatService.saveTextMessage(request.getChatroomId(), userId, request.getText());
        } else if (request.getType().equals("load")) {
            LocalDateTime time;
            try {
                time = LocalDateTime.parse(request.getText());
            } catch (DateTimeException e) {
                return;
            }
            chatService.getChats(request.getChatroomId(), Timestamp.valueOf(time));
        }

    }

    private void handleSendTextMessage(Long chatroomId, Long userId) {
        //
    }

    private void handleLoadTextMessage() {
        //
    }

    private void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        byte[] payload = message.getPayload().array();
        System.out.println("바이너리 메시지 길이(바이트): " + payload.length);
    }
}
