package com.s576air.webchat.repository;

import com.s576air.webchat.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        String sql = "SELECT id, login_id, password_hash, name FROM users WHERE login_id = ?";
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

    public Optional<User> findById(Long userId) {
        String sql = "SELECT id, login_id, password_hash, name FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper(), userId);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Map<Long, String> getIdNameMapByIds(List<Long> ids) {
        Map<Long, String> map = new HashMap<>();

        String sqlIds = "?,".repeat(ids.size());
        sqlIds = sqlIds.substring(0, sqlIds.length() - 1);
        String sql = "SELECT id, name FROM users WHERE id IN (" + sqlIds + ")";
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                map.put(id, name);
            }
        }, ids.toArray());

        return map;
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
