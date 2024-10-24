package com.s576air.webchat.repository;

import com.s576air.webchat.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertUser(User user) {
        String sql = "INSERT INTO users (login_id, password_hash, name) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getId(), user.getPasswordHash(), user.getName());
    }

    public void deleteUser(String id) {
        String sql = "DELETE FROM users WHERE login_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<User> findById(String id) {
        String sql = "SELECT login_id, password_hash, name FROM users WHERE login_id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper(), id);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (IncorrectResultSizeDataAccessException e) {
            System.err.println("Multiple users found with ID: " + id);
            return Optional.empty();
        }
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(
            rs.getString("login_id"),
            rs.getString("password_hash"),
            rs.getString("name")
        );
    }

    void clear() {
        String sql = "DELETE FROM users";
        jdbcTemplate.update(sql);
    }
}
