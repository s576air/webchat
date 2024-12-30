package com.s576air.webchat.domain;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ChatroomCache {
    private final ReentrantLock lock = new ReentrantLock();
    private String name;
    // value: userId
    private Set<Long> userIds = new HashSet<>();
    private ChatCache chatCache = new ChatCache();

    public ChatroomCache(String name) {
        this.name = name;
    }

    public void addUserId(Long userId) {
        lock.lock();

        userIds.add(userId);

        lock.unlock();
    }

    public boolean removeUserSession(Long userId) {
        lock.lock();

        boolean result = userIds.remove(userId);

        lock.unlock();

        return result;
    }

    public List<Long> getSessionIds() {
        lock.lock();

        List<Long> sessionIds = new ArrayList<>(userIds);

        lock.unlock();

        return sessionIds;
    }
}
