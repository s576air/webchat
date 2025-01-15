package com.s576air.webchat.domain;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ChatroomCache {
    private final ReentrantLock lock = new ReentrantLock();
    // value: userId
    private Set<Long> userIds;
    private ChatCache chatCache = new ChatCache();
    private Timestamp time = new Timestamp(System.currentTimeMillis());

    public ChatroomCache(List<Long> userIds) {
        this.userIds = new HashSet<>(userIds);
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

    public void add(Chat chat) {
        lock.lock();

        chatCache.add(chat);

        lock.unlock();
    }
}
