package com.s576air.webchat.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ChatroomCache {
    private ReentrantLock lock;
    // key: userId, value: sessionId
    private Map<Long, Long> userSessions = new HashMap<>();
    private ChatCache chatCache = new ChatCache();

    public void addUserSession(Long userId, Long sessionId) {
        lock.lock();

        userSessions.put(userId, sessionId);

        lock.unlock();
    }

    public boolean removeUserSession(Long userId) {
        lock.lock();

        Long result = userSessions.remove(userId);

        lock.unlock();

        return result != null;
    }

    public List<Long> getSessionIds() {
        lock.lock();

        List<Long> sessionIds = new ArrayList<>(userSessions.values());

        lock.unlock();

        return sessionIds;
    }
}
