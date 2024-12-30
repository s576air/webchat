package com.s576air.webchat.service;

import com.s576air.webchat.domain.ChatroomCache;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatroomsCache {
    // key: ChatroomId
    private ConcurrentHashMap<Long, ChatroomCache> cache = new ConcurrentHashMap<>();
}
