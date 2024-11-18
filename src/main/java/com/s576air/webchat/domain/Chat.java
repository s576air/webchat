package com.s576air.webchat.domain;

public class Chat {
    Long chatroomId;
    Long userId;
    // text 또는 확장자(txt, png, mp4 등)
    boolean type;
    // 텍스트는 채팅, 확장자는 url 표시
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(Long chatroomId) {
        this.chatroomId = chatroomId;
    }
}