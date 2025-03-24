package com.s576air.webchat.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.domain.CustomUserDetails;
import com.s576air.webchat.dto.LoadRequestPayload;
import com.s576air.webchat.dto.SendRequestPayload;
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
import java.util.Map;
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

    public static void sendTextChat(WebSocketSession session, Chat chat) {
        if (session != null && session.isOpen()) {
            Optional<String> chatJson = JsonUtil.toTaggedJson("newChat", chat);
            if (chatJson.isPresent()) {
                try {
                    session.sendMessage(new TextMessage(chatJson.get()));
                } catch (IOException e) {
                    System.out.println("session.sendMessage error: " + e.getMessage());
                }
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

        Map<String, Object> map;
        try {
            map = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            return Optional.of(JsonUtil.errorJson("요청 해석에 실패하였습니다."));
        }

        String type;
        Object objectType = map.get("type");
        if (objectType instanceof String) {
            type = (String) objectType;
        } else {
            return Optional.empty();
        }

        if (type.equals("send")) {
            Optional<SendRequestPayload> optionalSendRequestPayload = SendRequestPayload.fromMap(map);
            if (optionalSendRequestPayload.isEmpty())
                return Optional.of(JsonUtil.errorJson("메시지 전송 요청의 형식이 잘못되었습니다."));
            SendRequestPayload sendRequestPayload = optionalSendRequestPayload.get();

            if (!chatroomService.containsUser(sendRequestPayload.getChatroomId(), userId))
                return Optional.of(JsonUtil.errorJson("유저가 채팅방에 소속되어 있지 않습니다."));

            chatService.saveTextChat(sendRequestPayload.getChatroomId(), userId, sendRequestPayload.getText());
        } else if (type.equals("load")) {
            Optional<LoadRequestPayload> optionalLoadRequestPayload = LoadRequestPayload.fromMap(map);
            if (optionalLoadRequestPayload.isEmpty())
                return Optional.of(JsonUtil.errorJson("메시지 불러오기 요청의 형식이 잘못되었습니다."));
            LoadRequestPayload loadRequestPayload = optionalLoadRequestPayload.get();

            if (!chatroomService.containsUser(loadRequestPayload.getChatroomId(), userId))
                return Optional.of(JsonUtil.errorJson("유저가 채팅방에 소속되어 있지 않습니다."));

            Optional<List<Chat>> chats = chatService.getChats(loadRequestPayload.getChatroomId(), loadRequestPayload.getId());

            if (chats.isPresent()) {
                return JsonUtil.toTaggedJson("chats", chats.get());
            } else {
                return Optional.of(JsonUtil.errorJson("채팅 내역을 가져오지 못했습니다."));
            }
        }

        return Optional.empty();
    }

    private void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        byte[] payload = message.getPayload().array();
        System.out.println("바이너리 메시지 길이(바이트): " + payload.length);
    }
}
