package com.s576air.webchat.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChatCache {
    private ArrayList<Chat> chats = new ArrayList<>();

    public void add(Chat chat) {
        // chat id를 임시 값으로 갱신
        int size = chats.size();
        Long id;
        if (size == 0) {
            id = Long.MAX_VALUE / 2;
        } else {
            id = chats.get(size - 1).getId() + 1;
        }
        chat.setId(id);
        chats.add(chat);
    }
}
