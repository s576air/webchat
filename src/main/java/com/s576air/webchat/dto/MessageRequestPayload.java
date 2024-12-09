package com.s576air.webchat.dto;

public class MessageRequestPayload {
    // send: 클라이언트가 메시지 전송, load: 클라이언트가 특정 시간대의 메시지 요청
    String type;
    Long chatroomId;
    // send type: 전송할 메시지, load type: 시간을 나타내는 문자열
    String text;

    public MessageRequestPayload(Long chatroomId, String text) {
        this.chatroomId = chatroomId;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public Long getChatroomId() {
        return chatroomId;
    }

    public String getText() {
        return text;
    }
}
