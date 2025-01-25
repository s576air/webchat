package com.s576air.webchat.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.domain.CustomUserDetails;
import com.s576air.webchat.dto.MessageRequestPayload;
import com.s576air.webchat.service.ChatService;
import com.s576air.webchat.service.ChatroomService;
import com.s576air.webchat.service.UserService;
import com.s576air.webchat.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class ChatWebSocketHandler implements WebSocketHandler {
    private final ChatService chatService;
    private final ChatroomService chatroomService;
    private final UserService userService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public ChatWebSocketHandler(
        ChatService chatService,
        ChatroomService chatroomService,
        UserService userService
    ) {
        this.chatService = chatService;
        this.chatroomService = chatroomService;
        this.userService = userService;
    }

    public static void sendTextChat(WebSocketSession session, Chat chat) throws IOException {
        if (session != null && session.isOpen()) {
            Optional<String> chatJson = JsonUtil.toJson(chat);
            if (chatJson.isPresent()) {
                session.sendMessage(new TextMessage(chatJson.get()));
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Authentication authentication = (Authentication) session.getAttributes().get("user");
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();

            userService.cacheUserSession(userId, session);
        }
    }

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
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        Authentication authentication = (Authentication) session.getAttributes().get("user");
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Long userId = userDetails.getId();

            userService.removeUserSession(userId);
        }
    }

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
            Long chatroomId = request.getChatroomId();
            if (chatroomService.containsUser(chatroomId, userId)) {
                chatService.saveTextMessage(chatroomId, userId, request.getText());
                // 같은 채팅방의 다른 유저에게 메시지 전송하는 기능 추가바람
            }
            return Optional.empty();
        } else if (request.getType().equals("load")) {
            long id;
            try {
                id = Long.parseLong(request.getText());
            } catch (NumberFormatException e) {
                return Optional.of("{\"error\": \"숫자 해석 실패\"}");
            }
            Optional<List<Chat>> chats = chatService.getChats(request.getChatroomId(), id);

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
