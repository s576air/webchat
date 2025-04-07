package com.s576air.webchat.service;

import com.s576air.webchat.repository.FriendRepository;
import com.s576air.webchat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FriendService {
    UserRepository userRepository;
    FriendRepository friendRepository;

    @Autowired
    public FriendService(UserRepository userRepository, FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    public Map<Long, String> getFriendsIdNameMap(Long userId) {
        List<Long> friendIds = friendRepository.getFriendIds(userId);
        return userRepository.getIdNameMapByIds(friendIds);
    }
}
