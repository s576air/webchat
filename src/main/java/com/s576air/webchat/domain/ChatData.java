package com.s576air.webchat.domain;

import java.io.InputStream;

public class ChatData {
    InputStream data;
    String extension;

    public ChatData(InputStream data, String extension) {
        this.data = data;
        this.extension = extension;
    }

    public InputStream getData() {
        return data;
    }

    public void setData(InputStream data) {
        this.data = data;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
