package com.s576air.webchat.domain;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ChatroomCache {
    private final ReentrantLock lock = new ReentrantLock();
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
        Timestamp now = new Timestamp(System.currentTimeMillis());

        lock.lock();

        time = now;
        chatCache.add(chat);

        lock.unlock();
    }

    public Optional<List<Chat>> getChats(Long chatId, int limit) {
        return chatCache.getChats(chatId, limit);
    }

    public boolean isIdle() {
        long now = new Timestamp(System.currentTimeMillis()).getTime();

        lock.lock();

        long duration = now - time.getTime();

        lock.unlock();

        long idleDuration = 20 * 60 * 1000;
        return duration > idleDuration;
    }
}
