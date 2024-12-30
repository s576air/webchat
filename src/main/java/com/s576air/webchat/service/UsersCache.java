package com.s576air.webchat.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class UsersCache {
    // key: userId
    private ConcurrentHashMap<Long, UsersCache> cache;
}
