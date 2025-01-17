package com.s576air.webchat.domain;

import java.util.*;

public class ChatCache {
    private ArrayList<Chat> chats = new ArrayList<>();

    public void add(Chat chat) {
        chats.add(chat);
    }

    public Optional<List<Chat>> getChats(Long chatId, int limit) {
        Optional<Integer> optionalIndex = getIndexByChatId(chatId);
        if (optionalIndex.isPresent()) {
            int index = optionalIndex.get();
            List<Chat> sublist = chats.subList(Math.max(index - limit, 0), index);
            return Optional.of(sublist);
        } else {
            return Optional.empty();
        }
    }

    private Optional<Integer> getIndexByChatId(Long chatId) {
        int low = 0;
        int high = chats.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (chats.get(mid).getId().equals(chatId)) {
                return Optional.of(mid);
            } else if (chats.get(mid).getId() < chatId) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return Optional.empty();
    }
}
