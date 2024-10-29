package com.s576air.webchat.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class ChatroomParticipantsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChatroomParticipantsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean insert(Long chatroomId, Long userId) {
        String sql = "INSERT INTO chatroom_participants(chatroom_id, user_id) VALUES(?, ?)";
        int updatedRows = jdbcTemplate.update(sql, chatroomId, userId);
        return updatedRows >= 1;
    }

    public List<Long> findChatroomIdListByUserId(Long id) {
        String sql = "SELECT chatroom_id FROM chatroom_participants WHERE user_id = ?";
        try {
            return jdbcTemplate.query(sql, ChatroomIdRowMapper(), id);
        } catch (Exception e) {
            System.out.println("findChatroomListByUserId error: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private RowMapper<Long> ChatroomIdRowMapper() {
        return (rs, rowNum) -> rs.getLong("chatroom_id");
    }
}
