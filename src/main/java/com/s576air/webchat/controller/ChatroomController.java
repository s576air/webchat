package com.s576air.webchat.controller;

import com.s576air.webchat.domain.CustomUserDetails;
import com.s576air.webchat.service.ChatroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatroomController {
    private final ChatroomService chatroomService;

    @Autowired
    public ChatroomController(ChatroomService chatroomService) {
        this.chatroomService = chatroomService;
    }

    @PostMapping("chatroom")
    public String chatroomPage(@RequestParam("id") Long chatroomId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        if (chatroomService.containsUser(chatroomId, userId)) {
            model.addAttribute("id", chatroomId);
            return "chatroom";
        } else {
            return "chatroom-not-found.html";
        }
    }
}
