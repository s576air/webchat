package com.s576air.webchat.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ChatRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TextChatRepository textChatRepository;

    @Autowired
    public ChatRepository(JdbcTemplate jdbcTemplate, TextChatRepository textChatRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.textChatRepository = textChatRepository;
    }

    public boolean addTextChat(Long chatroomId, Long userId, String text) {
        Optional<Long> textChatId = textChatRepository.insert(text);
        if (textChatId.isEmpty()) { return false; }
        String sql = "INSERT INTO chat(chatroom_id, user_id, is_text, content_id) VALUES(?, ?, ?, ?)";
        jdbcTemplate.update(sql, chatroomId, userId, true, textChatId.get());
        return true;
    }
}
