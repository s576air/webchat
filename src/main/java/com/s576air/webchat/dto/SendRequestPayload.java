package com.s576air.webchat.dto;

import java.util.Map;
import java.util.Optional;

public class SendRequestPayload {
    Long chatroomId;
    String text;

    public static Optional<SendRequestPayload> fromMap(Map<String, Object> map) {
        SendRequestPayload payload = new SendRequestPayload();
        Object chatroomId = map.get("chatroomId");
        if (!(chatroomId instanceof Long)) return Optional.empty();

        Object text = map.get("text");
        if (!(text instanceof Long)) return Optional.empty();

        payload.chatroomId = (Long) chatroomId;
        payload.text = (String) text;

        return Optional.of(payload);
    }

    public Long getChatroomId() {
        return chatroomId;
    }

    public String getText() {
        return text;
    }
}
