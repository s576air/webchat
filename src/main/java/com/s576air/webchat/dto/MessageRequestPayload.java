package com.s576air.webchat.dto;

public class MessageRequestPayload {
    Long chatroomId;
    String text;

    public MessageRequestPayload(Long chatroomId, String text) {
        this.chatroomId = chatroomId;
        this.text = text;
    }

    public Long getChatroomId() {
        return chatroomId;
    }

    public String getText() {
        return text;
    }
}
