package com.s576air.webchat.service;

import com.s576air.webchat.domain.UserCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class UsersCache {
    // key: userId
    private ConcurrentHashMap<Long, UserCache> cache = new ConcurrentHashMap<>();

    public void insertUser(Long userId, UserCache userCache) {
        cache.put(userId, userCache);
    }

    public void removeUser(Long userId) {
        cache.remove(userId);
    }
}
