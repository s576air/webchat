package com.s576air.webchat.controller;

import com.s576air.webchat.service.FriendService;
import com.s576air.webchat.util.FriendCodeUtil;
import com.s576air.webchat.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class FriendController {
    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    private void addFriendInner(String friendCode) {
        Optional<Long> friendId = FriendCodeUtil.getId(friendCode);
        if (friendId.isEmpty()) return;
        Optional<String> friendTag = FriendCodeUtil.getTag(friendCode);
        if (friendTag.isEmpty()) return;

        Long userId = SecurityUtil.getUserId().orElseThrow();
        if (userId.equals(friendId.get())) return;

        friendService.addFriend(userId, friendId.get(), friendTag.get());
    }

    @PostMapping("addFriend")
    public String addFriend(@RequestParam("code") String friendCode) {
        if (friendCode != null) {
            addFriendInner(friendCode);
        }
        return "redirect:/friend-list";
    }


}
