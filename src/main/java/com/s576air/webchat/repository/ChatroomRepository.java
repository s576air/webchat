package com.s576air.webchat.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ChatroomRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChatroomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<String> getName(Long id) {
        String sql = "SELECT name FROM chatroom WHERE id = ?";
        try {
            String name = jdbcTemplate.queryForObject(sql, String.class, id);
            return Optional.ofNullable(name);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
