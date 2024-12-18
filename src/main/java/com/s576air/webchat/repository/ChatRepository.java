package com.s576air.webchat.repository;

import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.dto.ChatBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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

    public boolean addTextChat(Long chatroomId, Long userId, String text) {
        Optional<Long> textChatId = textChatRepository.insert(text);
        if (textChatId.isEmpty()) { return false; }
        String sql = "INSERT INTO chat(chatroom_id, user_id, is_text, content_id, sent_time) VALUES(?, ?, ?, ?, ?)";
        Timestamp time = new Timestamp(new Date().getTime()); // 정밀도 ms
        jdbcTemplate.update(sql, chatroomId, userId, true, textChatId.get(), time);
        return true;
    }

    public Optional<List<Chat>> getChats(Long chatroomId, Timestamp time, int limit) {
        String sql = "SELECT * FROM chat WHERE chatroom_id = ? AND sent_time < ? ORDER BY sent_time DESC LIMIT ?";
        try {
            List<ChatBase> chatBases = jdbcTemplate.query(sql, ChatBaseRowMapper(), chatroomId, time, limit);

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
            rs.getLong("chatroom_id"),
            rs.getLong("user_id"),
            rs.getBoolean("is_text"),
            rs.getLong("content_id"),
            rs.getTimestamp("sent_time").toLocalDateTime()
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
            chats.add(new Chat(base.getChatroomId(), base.getUserId(), "", texts.get(i), base.getSentTime()));
        }

        return Optional.of(chats);
    }

    private RowMapper<String> TextChatRowMapper() {
        return (rs, rowNum) -> rs.getString("text");
    }
}
