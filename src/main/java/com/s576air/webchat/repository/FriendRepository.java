package com.s576air.webchat.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FriendRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean is_friend(long userId, long userId2) {
        long id = Math.min(userId, userId2);
        long id2 = Math.max(userId, userId2);

        String sql = "SELECT COUNT(*) FROM friends WHERE id = ? AND id2 = ?";
        Integer result;
        result = jdbcTemplate.queryForObject(sql, Integer.class, id, id2);

        return result != null && result > 0;
    }

    public boolean add(long userId, long userId2) {
        long id = Math.min(userId, userId2);
        long id2 = Math.max(userId, userId2);

        String sql = "INSERT INTO friends(id, id2) VALUES (?, ?)";
        int result;
        result = jdbcTemplate.update(sql, id, id2);

        return result > 0;
    }

    public List<Long> getFriendIds(Long userId) {
        String sql = "SELECT CASE WHEN id = ? THEN id2 ELSE id END AS friend_id  FROM friends  WHERE id = ? OR id2 = ?";
        return jdbcTemplate.queryForList(sql, Long.class, userId, userId, userId);
    }
}
