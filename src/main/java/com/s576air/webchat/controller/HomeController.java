package com.s576air.webchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/friend-list")
    public String friendListPage() {
        return "friend-list";
    }

    @GetMapping("/chatroom-list")
    public String chatRoomListPage() {
        return "chatroom-list";
    }

    @GetMapping("/setting")
    public String settingPage() {
        return "setting";
    }
}
