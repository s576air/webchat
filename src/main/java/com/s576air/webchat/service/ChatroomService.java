package com.s576air.webchat.service;

import com.s576air.webchat.domain.ChatroomCache;
import com.s576air.webchat.repository.ChatroomParticipantsRepository;
import com.s576air.webchat.repository.ChatroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatroomService {
    private final ChatroomParticipantsRepository chatroomParticipantsRepository;
    private final ChatroomRepository chatroomRepository;
    // key: chatroomId
    private ConcurrentHashMap<Long, ChatroomCache> cache = new ConcurrentHashMap<>();

    @Autowired
    public ChatroomService(ChatroomParticipantsRepository chatroomParticipantsRepository, ChatroomRepository chatroomRepository) {
        this.chatroomParticipantsRepository = chatroomParticipantsRepository;
        this.chatroomRepository = chatroomRepository;
    }

    public boolean containsUser(Long chatroomId, Long userId) {
        return chatroomParticipantsRepository.isUserInChatroom(chatroomId, userId);
    }

    public Optional<String> getName(Long chatroomId) {
        return chatroomRepository.getName(chatroomId);
    }
}
