package com.s576air.webchat.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Chat {
    Long id;
    Long chatroomId;
    Long userId;
    // 비어 있으면 채팅 텍스트, 아니면 확장자(txt, png, mp4 등)
    String type;
    // 텍스트는 채팅, 확장자는 url 표시
    String content;
    Timestamp sentTime;

    public Chat() {}

    public Chat(Long id, Long chatroomId, Long userId, String type, String content, Timestamp sentTime) {
        this.id = id;
        this.chatroomId = chatroomId;
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.sentTime = sentTime;
    }

    public static Chat textChat(Long id, Long chatroomId, Long userId, String text, Timestamp sentTime) {
        Chat chat = new Chat();
        chat.id = id;
        chat.chatroomId = chatroomId;
        chat.userId = userId;
        chat.type = "";
        chat.content = text;
        chat.sentTime = sentTime;
        return chat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Timestamp getSentTime() {
        return sentTime;
    }

    public void setSentTime(Timestamp sentTime) {
        this.sentTime = sentTime;
    }
}