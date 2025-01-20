package com.s576air.webchat.repository;

import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.dto.ChatBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class ChatRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TextChatRepository textChatRepository;

    @Autowired
    public ChatRepository(JdbcTemplate jdbcTemplate, TextChatRepository textChatRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.textChatRepository = textChatRepository;
    }

    public Optional<Long> addTextChat(Long chatroomId, Long userId, String text, Timestamp time) {
        Optional<Long> textChatId = textChatRepository.insert(text);
        if (textChatId.isEmpty()) { return Optional.empty(); }
        String sql = "INSERT INTO chat(chatroom_id, user_id, is_text, content_id, sent_time) VALUES(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, chatroomId);
            ps.setLong(2, userId);
            ps.setBoolean(3, true);
            ps.setLong(4, textChatId.get());
            ps.setTimestamp(5, time);
            return ps;
        }, keyHolder);

        try {
            Long id = ((Number) keyHolder.getKeys().get("id")).longValue();
            return Optional.of(id);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }

    public Optional<List<Chat>> getChats(Long chatroomId, long chatId, int limit) {
        String sql = "SELECT * FROM chat WHERE chatroom_id = ? AND id < ? ORDER BY id DESC LIMIT ?";
        try {
            List<ChatBase> chatBases = jdbcTemplate.query(sql, ChatBaseRowMapper(), chatroomId, chatId, limit);

            List<ChatBase> textBases = new ArrayList<>();
            List<ChatBase> binaryBases = new ArrayList<>();

            for (ChatBase chatBase: chatBases) {
                if (chatBase.isText()) {
                    textBases.add(chatBase);
                } else {
                    binaryBases.add(chatBase);
                }
            }

            Optional<List<Chat>> textChats = getTextChats(textBases);
            Optional<List<Chat>> binaryChats = Optional.of(new ArrayList<>());

            if (textChats.isEmpty() || binaryChats.isEmpty()) { return Optional.empty(); }

            // 나중에 병합 정렬로 수정 바람
            List<Chat> chats = textChats.get();

            return Optional.of(chats);

        } catch (Exception e) {
            System.out.println("findChatroomListByUserId error: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<List<Chat>> getLastChats(Long chatroomId, int limit) {
        String sql = "SELECT * FROM chat WHERE chatroom_id = ? ORDER BY sent_time DESC LIMIT ?";
        try {
            List<ChatBase> chatBases = jdbcTemplate.query(sql, ChatBaseRowMapper(), chatroomId, limit);

            List<ChatBase> textBases = new ArrayList<>();
            List<ChatBase> binaryBases = new ArrayList<>();

            for (ChatBase chatBase: chatBases) {
                if (chatBase.isText()) {
                    textBases.add(chatBase);
                } else {
                    binaryBases.add(chatBase);
                }
            }

            Optional<List<Chat>> textChats = getTextChats(textBases);
            Optional<List<Chat>> binaryChats = Optional.of(new ArrayList<>());

            if (textChats.isEmpty() || binaryChats.isEmpty()) { return Optional.empty(); }

            // 나중에 병합 정렬로 수정 바람
            List<Chat> chats = textChats.get();

            return Optional.of(chats);

        } catch (Exception e) {
            System.out.println("findChatroomListByUserId error: " + e.getMessage());
            return Optional.empty();
        }
    }

    private RowMapper<ChatBase> ChatBaseRowMapper() {
        return (rs, rowNum) -> new ChatBase(
            rs.getLong("id"),
            rs.getLong("chatroom_id"),
            rs.getLong("user_id"),
            rs.getBoolean("is_text"),
            rs.getLong("content_id"),
            rs.getTimestamp("sent_time")
        );
    }

    private Optional<List<Chat>> getTextChats(List<ChatBase> textBases) {
        if (textBases.isEmpty()) { return Optional.of(new ArrayList<>()); }

        int n = textBases.size() - 1;
        String sql = "SELECT * FROM text_chat WHERE id IN (?" + ",?".repeat(n) + ") ORDER BY id DESC";

        List<Long> contentIds = new ArrayList<>();
        for (ChatBase chatBase: textBases) {
            contentIds.add(chatBase.getContentId());
        }

        List<String> texts = jdbcTemplate.query(sql, TextChatRowMapper(), contentIds.toArray());

        if (textBases.size() != texts.size()) { return Optional.empty(); }

        List<Chat> chats = new ArrayList<>();

        for (int i = 0; i < textBases.size(); i++) {
            ChatBase base = textBases.get(i);
            chats.add(new Chat(base.getId(), base.getChatroomId(), base.getUserId(), "", texts.get(i), base.getSentTime()));
        }

        return Optional.of(chats);
    }

    private RowMapper<String> TextChatRowMapper() {
        return (rs, rowNum) -> rs.getString("text");
    }
}
