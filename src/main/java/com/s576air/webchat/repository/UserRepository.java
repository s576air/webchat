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

    public void insertUser(String id, String name) {
        String sql = "INSERT INTO users (id, name) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, name);
    }

    public void deleteUser(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void findById(String id) {
        String sql = "SELECT id, password_hash, password_salt FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    void clear() {
        String sql = "DELETE FROM users";
        jdbcTemplate.update(sql);
    }
}
