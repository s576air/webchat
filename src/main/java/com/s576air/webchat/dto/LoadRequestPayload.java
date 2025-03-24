package com.s576air.webchat.dto;

import java.util.Map;
import java.util.Optional;

public class LoadRequestPayload {
    Long chatroomId;
    // 클라이언트에서 로드된 첫 채팅의 id
    Long id;

    public static Optional<LoadRequestPayload> fromMap(Map<String, Object> map) {
        LoadRequestPayload payload = new LoadRequestPayload();
        Object chatroomId = map.get("chatroomId");
        if (!(chatroomId instanceof Number)) return Optional.empty();

        Object id = map.get("id");
        if (!(id instanceof Number)) return Optional.empty();

        payload.chatroomId = ((Number) chatroomId).longValue();
        payload.id = ((Number) id).longValue();

        return Optional.of(payload);
    }

    public Long getChatroomId() {
        return chatroomId;
    }

    public Long getId() {
        return id;
    }
}
