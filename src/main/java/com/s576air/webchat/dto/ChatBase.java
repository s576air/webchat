package com.s576air.webchat.dto;

import java.time.LocalDateTime;

public class ChatBase {
    private Long chatroomId;
    private Long userId;
    private boolean isText;
    private Long contentId;
    private LocalDateTime sentTime;

    public ChatBase(Long chatroomId, Long userId, boolean isText, Long contentId, LocalDateTime sentTime) {
        this.chatroomId = chatroomId;
        this.userId = userId;
        this.isText = isText;
        this.contentId = contentId;
        this.sentTime = sentTime;
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

    public boolean isText() {
        return isText;
    }

    public void setText(boolean text) {
        isText = text;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }
}
