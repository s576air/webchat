package com.s576air.webchat.dto;

import java.util.Map;
import java.util.Optional;

public class SendRequestPayload {
    Long chatroomId;
    String text;

    public static Optional<SendRequestPayload> fromMap(Map<String, Object> map) {
        SendRequestPayload payload = new SendRequestPayload();
        Object chatroomId = map.get("chatroomId");
        if (!(chatroomId instanceof Number)) return Optional.empty();

        Object text = map.get("text");
        if (!(text instanceof String)) return Optional.empty();

        payload.chatroomId = ((Number) chatroomId).longValue();
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
