package com.s576air.webchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatroomController {
    @PostMapping("chatroom")
    public String chatroomPage(@RequestParam("id") Long id, Model model) {
        model.addAttribute("id", id);
        return "chatroom";
    }
}
