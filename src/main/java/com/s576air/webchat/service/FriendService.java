package com.s576air.webchat.service;

import com.s576air.webchat.repository.FriendRepository;
import com.s576air.webchat.repository.UserRepository;
import com.s576air.webchat.util.FriendCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FriendService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    @Autowired
    public FriendService(UserRepository userRepository, FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    public Map<Long, String> getFriendsIdNameMap(Long userId) {
        List<Long> friendIds = friendRepository.getFriendIds(userId);
        return userRepository.getIdNameMapByIds(friendIds);
    }

    public Optional<String> getFriendCode(Long userId) {
        Optional<String> tag = userRepository.getFriendCodeTag(userId);
        if (tag.isEmpty()) { return Optional.empty(); }

        String encodedId = FriendCodeUtil.encodeId(userId);
        return Optional.of(encodedId + tag.get());
    }

    public void addFriend(long selfId, long friendId, String friendTag) {
        Optional<String> friendTag2 = userRepository.getFriendCodeTag(friendId);
        if (friendTag2.isPresent() && friendTag2.get().equals(friendTag)) {
            friendRepository.add(selfId, friendId);
        }
    }
}
