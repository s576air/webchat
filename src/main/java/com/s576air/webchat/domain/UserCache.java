package com.s576air.webchat.domain;

import java.util.Optional;

public class UserCache {
    private String name;
    private Optional<String> sessionId;

    public UserCache(String name, Optional<String> sessionId) {
        this.name = name;
        this.sessionId = sessionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<String> getSessionId() {
        return sessionId;
    }

    public void setSessionId(Optional<String> sessionId) {
        this.sessionId = sessionId;
    }
}
