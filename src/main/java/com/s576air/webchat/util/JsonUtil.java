package com.s576air.webchat.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Map;
import java.util.Optional;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper() {{
        registerModule(new JavaTimeModule());
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }};

    public static Optional<String> toJson(Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            return Optional.of(json);
        } catch (Exception e) {
            System.out.println("json 변환 오류:" + e.getMessage());
            return Optional.empty();
        }
    }

    public static Optional<String> toTaggedJson(String tag, Object object) {
        // tag 종류: newChat, chats
        return JsonUtil.toJson(Map.of("tag", tag, "content", object));
    }

    public static String errorJson(String message) {
        return """
            { "tag": "error", "content": "
            """.strip() + message + "\"}";
    }
}
