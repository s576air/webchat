package com.s576air.webchat.domain;

public class LoadChatData {
    Long id;
    String extention;

    public LoadChatData(Long id, String extention) {
        this.id = id;
        this.extention = extention;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExtention() {
        return extention;
    }

    public void setExtention(String extention) {
        this.extention = extention;
    }
}
