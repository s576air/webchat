package com.s576air.webchat.domain;

import java.io.InputStream;

public class ChatData {
    InputStream data;
    // 예시: video/mp4, audio/ogg
    String contentType;

    public ChatData(InputStream data, String extension) {
        this.data = data;
        this.contentType = extension;
    }

    public InputStream getData() {
        return data;
    }

    public void setData(InputStream data) {
        this.data = data;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
