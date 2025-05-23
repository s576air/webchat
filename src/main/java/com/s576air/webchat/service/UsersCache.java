package com.s576air.webchat.service;

import com.s576air.webchat.domain.UserCache;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Optional;
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

    public Optional<WebSocketSession> getSession(Long userId) {
        UserCache userCache = cache.get(userId);
        if (userCache == null) {
            return Optional.empty();
        } else {
            return userCache.getSession();
        }
    }

    public void setSession(Long userId, Optional<WebSocketSession> session) {
        cache.compute(userId, (key, value) -> {
            if (value != null) {
                value.setSession(session);
            }

            return value;
        });
    }

    public void addUserChatroomId(Long userId, Long chatroomId) {
        cache.compute(userId, (key, value) -> {
            if (value != null) {
                value.addChatroomId(chatroomId);
            }

            return value;
        });
    }

    public Optional<List<Long>> getChatroomIdsByUserId(Long userId) {
        UserCache userCache = cache.get(userId);
        if (userCache == null) {
            return Optional.empty();
        } else {
            return Optional.of(userCache.getChatroomIds());
        }
    }

    public Optional<Boolean> isUserInChatroom(Long userId, Long chatroomId) {
        UserCache userCache = cache.get(userId);
        if (userCache == null) {
            return Optional.empty();
        } else {
            return Optional.of(userCache.isInChatroom(chatroomId));
        }
    }

    public void addUsedChatroomId(Long userId, Long chatroomId) {
        cache.compute(userId, (key, value) -> {
            if (value != null) {
                value.addUsedChatroomId(chatroomId);
            }

            return value;
        });
    }

    public Optional<List<Long>> getUsedChatroomIds(Long userId) {
        UserCache userCache = cache.get(userId);
        if (userCache == null) {
            return Optional.empty();
        } else {
            return Optional.of(userCache.getUsedChatroomIds());
        }
    }
}
