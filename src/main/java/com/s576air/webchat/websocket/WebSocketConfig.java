package com.s576air.webchat.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatWebSocketHandler chatWebSocketHandler;
    private final SecurityHandshakeInterceptor securityHandshakeInterceptor;

    @Autowired
    public WebSocketConfig(ChatWebSocketHandler chatWebSocketHandler, SecurityHandshakeInterceptor securityHandshakeInterceptor) {
        this.chatWebSocketHandler = chatWebSocketHandler;
        this.securityHandshakeInterceptor = securityHandshakeInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/ws/chat")
                .addInterceptors(securityHandshakeInterceptor)
                .setAllowedOrigins("*");
    }
}