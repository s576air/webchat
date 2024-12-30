package com.s576air.webchat.domain;

import java.util.Optional;

public class UserCache {
    private String name;
    private Optional<Long> sessionId;

    public UserCache(String name, Optional<Long> sessionId) {
        this.name = name;
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public Optional<Long> getSessionId() {
        return sessionId;
    }
}
