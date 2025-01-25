package com.s576air.webchat.domain;

import org.springframework.web.socket.WebSocketSession;

import java.util.*;

public class UserCache {
    private Set<Long> chatroomIds;
    private Set<Long> usedChatroomIds;
    private Optional<WebSocketSession> session;

    public UserCache(List<Long> chatroomIds, Optional<WebSocketSession> session) {
        this.chatroomIds = new HashSet<>(chatroomIds);
        this.usedChatroomIds = new HashSet<>();
        this.session = session;
    }

    public Optional<WebSocketSession> getSession() {
        return session;
    }

    public void setSession(Optional<WebSocketSession> session) {
        this.session = session;
    }

    public List<Long> getChatroomIds() {
        return new ArrayList<>(chatroomIds);
    }

    public boolean isInChatroom(Long chatroomId) {
        return chatroomIds.contains(chatroomId);
    }

    public void addUsedChatroomId(Long chatroomId) {
        usedChatroomIds.add(chatroomId);
    }

    public List<Long> getUsedChatroomIds() {
        return new ArrayList<>(usedChatroomIds);
    }
}
