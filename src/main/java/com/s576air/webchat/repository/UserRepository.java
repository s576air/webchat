package com.s576air.webchat.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    void insertUser(Long id, String name) {
        String sql = "INSERT INTO users (id, imagePath, name) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, id, name);
    }
}
