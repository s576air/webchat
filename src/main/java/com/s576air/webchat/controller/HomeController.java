package com.s576air.webchat.controller;

import com.s576air.webchat.domain.SimpleChatroom;
import com.s576air.webchat.service.FriendService;
import com.s576air.webchat.service.UserService;
import com.s576air.webchat.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    private final UserService userService;
    private final FriendService friendService;

    @Autowired
    public HomeController(UserService userService, FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    @GetMapping("/")
    public String redirectPage() {
        return "redirect:/friend-list";
    }

    @GetMapping("/friend-list")
    public String friendListPage(Model model) {
        Long userId = SecurityUtil.getUserId().orElseThrow();

        Map<Long, String> friendsIdNameMap = friendService.getFriendsIdNameMap(userId);
        String code = friendService.getFriendCode(userId).orElse("");

        model.addAttribute("map", friendsIdNameMap);
        model.addAttribute("friendCode", code);

        return "friend-list";
    }

    @GetMapping("/chatroom-list")
    public String chatRoomListPage(Model model) {
        Long id = SecurityUtil.getUserId().orElseThrow();

        List<SimpleChatroom> chatroomList = userService.getSimpleChatroomList(id);
        model.addAttribute("chatroomList", chatroomList);
        return "chatroom-list";
    }

    @GetMapping("/setting")
    public String settingPage() {
        return "setting";
    }
}
