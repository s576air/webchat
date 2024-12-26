package com.s576air.webchat.security;

import com.s576air.webchat.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ChatService chatService;

    @Autowired
    public CustomAuthenticationSuccessHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) {
        // chatService.addUserSessionForChatrooms(userId, sessionId);
    }
}
