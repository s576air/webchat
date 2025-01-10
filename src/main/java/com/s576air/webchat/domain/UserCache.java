package com.s576air.webchat.domain;

import java.util.*;

public class UserCache {
    private Set<Long> chatroomIds;
    private Optional<String> sessionId;

    public UserCache(List<Long> chatroomIds, Optional<String> sessionId) {
        this.chatroomIds = new HashSet<>(chatroomIds);
        this.sessionId = sessionId;
    }

    public Optional<String> getSessionId() {
        return sessionId;
    }

    public void setSessionId(Optional<String> sessionId) {
        this.sessionId = sessionId;
    }

    public List<Long> getChatroomIds() {
        return new ArrayList<>(chatroomIds);
    }

    public boolean isInChatroom(Long chatroomId) {
        return chatroomIds.contains(chatroomId);
    }
}
