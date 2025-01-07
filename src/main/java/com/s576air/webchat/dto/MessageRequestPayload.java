package com.s576air.webchat.dto;

public class MessageRequestPayload {
    // send: 클라이언트가 메시지 전송, load: 클라이언트가 특정 시간대의 메시지 요청
    String type;
    Long chatroomId;
    // send type: 전송할 메시지, load type: 아이디를 나타내는 문자열
    String text;

    public MessageRequestPayload() {}

    public MessageRequestPayload(String type, Long chatroomId, String text) {
        this.type = type;
        this.chatroomId = chatroomId;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(Long chatroomId) {
        this.chatroomId = chatroomId;
    }
}
