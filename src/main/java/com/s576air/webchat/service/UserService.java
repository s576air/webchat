package com.s576air.webchat.service;

import com.s576air.webchat.domain.SimpleChatroom;
import com.s576air.webchat.domain.User;
import com.s576air.webchat.domain.UserCache;
import com.s576air.webchat.repository.ChatroomParticipantsRepository;
import com.s576air.webchat.repository.ChatroomRepository;
import com.s576air.webchat.repository.UserRepository;
import com.s576air.webchat.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatroomParticipantsRepository chatroomParticipantsRepository;
    private final UsersCache usersCache;

    @Autowired
    public UserService(
        UserRepository userRepository,
        ChatroomRepository chatroomRepository,
        ChatroomParticipantsRepository chatroomParticipantsRepository,
        UsersCache usersCache
    ) {
        this.userRepository = userRepository;
        this.chatroomRepository = chatroomRepository;
        this.chatroomParticipantsRepository = chatroomParticipantsRepository;
        this.usersCache = usersCache;
    }

    public boolean signUp(String login_id, String password) {
        if (userRepository.findByLoginId(login_id).isPresent()) {
            return false;
        }
        String passwordHash = PasswordUtil.hashPassword(password);

        return userRepository.insertUser(login_id, passwordHash, "default").isPresent();
    }

    public List<SimpleChatroom> getSimpleChatroomList(Long user_id) {
        List<Long> chatroomIdList = chatroomParticipantsRepository.findChatroomIdListByUserId(user_id);
        List<SimpleChatroom> chatroomList = new ArrayList<>(chatroomIdList.size());

        for (Long chatroomId: chatroomIdList) {
            Optional<String> name = chatroomRepository.getName(chatroomId);

            if (name.isPresent()) {
                SimpleChatroom chatroom = new SimpleChatroom(chatroomId, name.get());
                chatroomList.add(chatroom);
            }
        }

        return chatroomList;
    }

    public void cacheUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            UserCache userCache = new UserCache(user.get().getName(), Optional.empty());
            usersCache.insertUser(userId, userCache);
        }
    }

    public void removeCacheUser(Long userId) {
        usersCache.removeUser(userId);
    }
}
