package com.s576air.webchat.controller;

import com.s576air.webchat.domain.CustomUserDetails;
import com.s576air.webchat.service.ChatService;
import com.s576air.webchat.service.ChatroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ChatroomController {
    private final ChatroomService chatroomService;
    private final ChatService chatService;

    @Autowired
    public ChatroomController(ChatroomService chatroomService, ChatService chatService) {
        this.chatroomService = chatroomService;
        this.chatService = chatService;
    }

    @PostMapping("chatroom")
    public String chatroomPage(@RequestParam("id") Long chatroomId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        Optional<String> chatroomName = chatroomService.getName(chatroomId);
        if (
            chatroomService.containsUser(chatroomId, userId) &&
            chatroomName.isPresent()
        ) {
            model.addAttribute("id", chatroomId);
            model.addAttribute("name", chatroomName.get());
            model.addAttribute("chats", chatService.getLastChats(chatroomId));
            return "chatroom";
        } else {
            return "forward:/chatroom-not-found.html";
        }
    }
}
