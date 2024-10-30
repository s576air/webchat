package com.s576air.webchat.service;

import com.s576air.webchat.repository.ChatroomParticipantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatroomService {
    private final ChatroomParticipantsRepository chatroomParticipantsRepository;

    @Autowired
    public ChatroomService(ChatroomParticipantsRepository chatroomParticipantsRepository) {
        this.chatroomParticipantsRepository = chatroomParticipantsRepository;
    }

    public boolean containsUser(Long chatroomId, Long userId) {
        return chatroomParticipantsRepository.isUserInChatroom(chatroomId, userId);
    }
}
