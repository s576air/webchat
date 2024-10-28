package com.s576air.webchat.controller;

import com.s576air.webchat.domain.CustomUserDetails;
import com.s576air.webchat.domain.SimpleChatroom;
import com.s576air.webchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/friend-list")
    public String friendListPage() {
        return "friend-list";
    }

    @GetMapping("/chatroom-list")
    public String chatRoomListPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getId();

        List<SimpleChatroom> chatroomList = userService.getSimpleChatroomList(id);
        model.addAttribute("chatroomList", chatroomList);
        return "chatroom-list";
    }

    @GetMapping("/setting")
    public String settingPage() {
        return "setting";
    }
}
