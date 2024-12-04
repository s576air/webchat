package com.s576air.webchat.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Optional<String> toJson(Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            return Optional.of(json);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
