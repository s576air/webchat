package com.s576air.webchat.service;

import com.s576air.webchat.repository.ChatroomParticipantsRepository;
import com.s576air.webchat.repository.ChatroomRepository;
import com.s576air.webchat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ChatroomService {
    private final ChatroomParticipantsRepository chatroomParticipantsRepository;
    private final ChatroomRepository chatroomRepository;
    private final UserRepository userRepository;
    private final UsersCache usersCache;

    @Autowired
    public ChatroomService(
        ChatroomParticipantsRepository chatroomParticipantsRepository,
        ChatroomRepository chatroomRepository,
        UserRepository userRepository,
        UsersCache usersCache
    ) {
        this.chatroomParticipantsRepository = chatroomParticipantsRepository;
        this.chatroomRepository = chatroomRepository;
        this.userRepository = userRepository;
        this.usersCache = usersCache;
    }

    public boolean insert(String name, List<Long> ids) {
        Optional<Long> optionalChatroomId = chatroomRepository.insert(name);
        if (optionalChatroomId.isEmpty()) return false;
        Long chatroomId = optionalChatroomId.get();
        for (Long id: ids) { System.out.println("개별:" + id);
            chatroomParticipantsRepository.insert(chatroomId, id);
        }
        return true;
    }

    public boolean containsUser(Long chatroomId, Long userId) {
        Optional<Boolean> userInChatroom = usersCache.isUserInChatroom(chatroomId, userId);
        return userInChatroom.orElseGet(() -> chatroomParticipantsRepository.isUserInChatroom(chatroomId, userId));
    }

    public Optional<String> getName(Long chatroomId) {
        return chatroomRepository.getName(chatroomId);
    }

    public Map<Long, String> getUsers(Long chatroomId) {
        List<Long> ids = chatroomParticipantsRepository.findUserIdListByChatroomId(chatroomId);
        return userRepository.getIdNameMapByIds(ids);
    }
}
