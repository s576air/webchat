package com.s576air.webchat.repository;

import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.domain.ChatData;
import com.s576air.webchat.domain.LoadChatData;
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
import java.util.List;
import java.util.Optional;

@Repository
public class ChatRepository {
    private final JdbcTemplate jdbcTemplate;
    private final TextChatRepository textChatRepository;
    private final DataChatRepository dataChatRepository;

    @Autowired
    public ChatRepository(JdbcTemplate jdbcTemplate, TextChatRepository textChatRepository, DataChatRepository dataChatRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.textChatRepository = textChatRepository;
        this.dataChatRepository = dataChatRepository;
    }

    public Optional<Long> addDataChat(Long chatroomId, Long userId, ChatData chatData, Timestamp time) {
        Optional<Long> dataChatId = dataChatRepository.insert(chatData);
        if (dataChatId.isEmpty()) { return Optional.empty(); }
        String sql = "INSERT INTO chat(chatroom_id, user_id, is_text, content_id, sent_time) VALUES(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, chatroomId);
            ps.setLong(2, userId);
            ps.setBoolean(3, false);
            ps.setLong(4, dataChatId.get());
            ps.setTimestamp(5, time);
            return ps;
        });

        try {
            Long id = ((Number) keyHolder.getKeys().get("id")).longValue();
            return Optional.of(id);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
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

            return convertChatBasesToChats(chatBases);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("findChatroomListByUserId error: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<List<Chat>> getLastChats(Long chatroomId, int limit) {
        String sql = "SELECT * FROM chat WHERE chatroom_id = ? ORDER BY sent_time DESC LIMIT ?";
        try {
            List<ChatBase> chatBases = jdbcTemplate.query(sql, ChatBaseRowMapper(), chatroomId, limit);

            return convertChatBasesToChats(chatBases);
        } catch (Exception e) {
            System.out.println("findChatroomListByUserId error: " + e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<List<Chat>> convertChatBasesToChats(List<ChatBase> chatBases) {
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
        Optional<List<Chat>> binaryChats = getDataChats(binaryBases);

        if (textChats.isEmpty() && binaryChats.isEmpty()) {
            return Optional.empty();
        } else if (textChats.isEmpty()) {
            return binaryChats;
        } else if (binaryChats.isEmpty()) {
            return textChats;
        }

        List<Chat> textChats2 = textChats.get();
        List<Chat> binaryChats2 = binaryChats.get();
        int textIndex = 0;
        int binaryIndex = 0;

        List<Chat> chats = new ArrayList<>();

        while (textChats2.size() > textIndex && binaryChats2.size() > binaryIndex) {
            Chat textChat = textChats2.get(textIndex);
            Chat binaryChat = binaryChats2.get(binaryIndex);
            if (textChat.getId() > binaryChat.getId()) {
                chats.add(textChat);
                textIndex++;
            } else {
                chats.add(binaryChat);
                binaryIndex++;
            }
        }

        chats.addAll(textChats2.subList(textIndex, textChats2.size()));
        chats.addAll(binaryChats2.subList(binaryIndex, binaryChats2.size()));

        return Optional.of(chats);
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

        List<Long> contentIds = textBases.stream().map(ChatBase::getContentId).toList();

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

    private Optional<List<Chat>> getDataChats(List<ChatBase> binaryBases) {
        if (binaryBases.isEmpty()) { return Optional.empty(); }

        int n = binaryBases.size() - 1;
        String sql = "SELECT * FROM data_chat WHERE id IN (?" + ",?".repeat(n) + ") ORDER BY id DESC";

        List<Long> contentIds = binaryBases.stream().map(ChatBase::getContentId).toList();

        List<LoadChatData> datas = jdbcTemplate.query(sql, LoadChatDataRowMapper(), contentIds.toArray());

        if (binaryBases.size() != datas.size()) { return Optional.empty(); }

        List<Chat> chats = new ArrayList<>();

        for (int i = 0; i < binaryBases.size(); i++) {
            ChatBase base = binaryBases.get(i);
            LoadChatData data = datas.get(i);
            chats.add(new Chat(base.getId(), base.getChatroomId(), base.getUserId(), data.getExtention(), data.getId().toString(), base.getSentTime()));
        }

        return Optional.of(chats);
    }

    private RowMapper<LoadChatData> LoadChatDataRowMapper() {
        return (rs, rowNum) -> new LoadChatData(
            rs.getLong("id"),
            rs.getString("ext")
        );
    }

    public Optional<ChatData> getChatData(Long dataChatId) {
        String sql = "SELECT * FROM data_chat WHERE id = ?";
        try {
            ChatData chatData = jdbcTemplate.queryForObject(sql, ChatDataRowMapper(), dataChatId);
            return Optional.ofNullable(chatData);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private RowMapper<ChatData> ChatDataRowMapper() {
        return (rs, rowNum) -> new ChatData(
            rs.getBinaryStream("data"),
            rs.getString("ext")
        );
    }
}
