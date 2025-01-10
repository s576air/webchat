package com.s576air.webchat.service;

import com.s576air.webchat.repository.ChatroomParticipantsRepository;
import com.s576air.webchat.repository.ChatroomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatroomService {
    private final ChatroomParticipantsRepository chatroomParticipantsRepository;
    private final ChatroomRepository chatroomRepository;
    private final UsersCache usersCache;

    @Autowired
    public ChatroomService(
        ChatroomParticipantsRepository chatroomParticipantsRepository,
        ChatroomRepository chatroomRepository,
        UsersCache usersCache
    ) {
        this.chatroomParticipantsRepository = chatroomParticipantsRepository;
        this.chatroomRepository = chatroomRepository;
        this.usersCache = usersCache;
    }

    public boolean containsUser(Long chatroomId, Long userId) {
        Optional<Boolean> userInChatroom = usersCache.isUserInChatroom(chatroomId, userId);
        return userInChatroom.orElseGet(() -> chatroomParticipantsRepository.isUserInChatroom(chatroomId, userId));
    }

    public Optional<String> getName(Long chatroomId) {
        return chatroomRepository.getName(chatroomId);
    }
}
