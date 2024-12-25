package com.s576air.webchat.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class ChatroomCache {
    private ReentrantLock lock;
    // key: userId, value: sessionId
    private Map<Long, Long> userSessions = new HashMap<>();
    private ChatCache chatCache = new ChatCache();
}
