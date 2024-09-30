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

    public void insertUser(Long id, String name) {
        String sql = "INSERT INTO users (id, name) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, name);
    }

    public void deleteUser(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void findById(Long id) {
        String sql = "SELECT id FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    void clear() {
        String sql = "DELETE FROM users";
        jdbcTemplate.update(sql);
    }
}
