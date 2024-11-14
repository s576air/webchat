package com.s576air.webchat.websocket;

import com.s576air.webchat.domain.CustomUserDetails;
import com.s576air.webchat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

@Component
public class ChatWebSocketHandler implements WebSocketHandler {
    private final ChatService chatService;

    @Autowired
    public ChatWebSocketHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {}

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            handleTextMessage(session, (TextMessage) message);
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

    private void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        System.out.println("텍스트 메시지: " + payload);

        // 1. 스프링 시큐리터의 인증으로 유저 아이디를 가져온다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        // 2. 메시지로부터 챗룸 id와 텍스트를 얻어온다.
        // 3. 메시지를 저장한다.
        // chatService.saveTextMessage(chatroomId, userId, text);
    }

    private void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        byte[] payload = message.getPayload().array();
        System.out.println("바이너리 메시지 길이(바이트): " + payload.length);
    }
}
