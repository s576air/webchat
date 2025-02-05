package com.s576air.webchat.repository;

import com.s576air.webchat.domain.ChatData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
public class DataChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Long> insert(ChatData chatData) {
        String sql = "INSERT INTO data_chat(ext, data) VALUES(?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, chatData.getExtension());
            ps.setBlob(2, chatData.getData());

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
