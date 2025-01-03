package com.s576air.webchat.domain;

import java.time.LocalDateTime;

public class Chat {
    Long chatroomId;
    Long userId;
    // 비어 있으면 채팅 텍스트 아니면 확장자(txt, png, mp4 등)
    String type;
    // 텍스트는 채팅, 확장자는 url 표시
    String content;
    LocalDateTime sentTime;

    public Chat() {}

    public Chat(Long chatroomId, Long userId, String type, String content, LocalDateTime sentTime) {
        this.chatroomId = chatroomId;
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.sentTime = sentTime;
    }

    public Long getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(Long chatroomId) {
        this.chatroomId = chatroomId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }
}