package com.s576air.webchat.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class TextChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TextChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Long> insert(String text) {
        String sql = "INSERT INTO text_chat(text) VALUES(?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, text);
            return ps;
        }, keyHolder);

        try {
            Long id = keyHolder.getKey().longValue();
            return Optional.of(id);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

}
