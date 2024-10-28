package com.s576air.webchat.repository;

import com.s576air.webchat.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Long> insertUser(String loginId, String passwordHash, String name) {
        String sql = "INSERT INTO users (login_id, password_hash, name) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, loginId);
            ps.setString(2, passwordHash);
            ps.setString(3, name);
            return ps;
        }, keyHolder);

        try {
            Long id = keyHolder.getKey().longValue();
            return Optional.of(id);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    public boolean deleteUser(String loginId) {
        String sql = "DELETE FROM users WHERE login_id = ?";
        int updatedRows = jdbcTemplate.update(sql, loginId);
        return updatedRows >= 1;
    }

    public Optional<User> findByLoginId(String loginId) {
        String sql = "SELECT login_id, password_hash, name FROM users WHERE login_id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper(), loginId);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (IncorrectResultSizeDataAccessException e) {
            System.err.println("Multiple users found with ID: " + loginId);
            return Optional.empty();
        }
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> new User(
            rs.getLong("id"),
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
