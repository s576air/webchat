package com.s576air.webchat.controller;

import com.s576air.webchat.domain.Chat;
import com.s576air.webchat.domain.CustomUserDetails;
import com.s576air.webchat.service.ChatService;
import com.s576air.webchat.service.ChatroomService;
import com.s576air.webchat.service.UsersCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class ChatroomController {
    private final ChatroomService chatroomService;
    private final ChatService chatService;
    private final UsersCache usersCache;

    @Autowired
    public ChatroomController(ChatroomService chatroomService, ChatService chatService, UsersCache usersCache) {
        this.chatroomService = chatroomService;
        this.chatService = chatService;
        this.usersCache = usersCache;
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
            usersCache.addUsedChatroomId(userId, chatroomId);

            model.addAttribute("id", chatroomId);
            model.addAttribute("name", chatroomName.get());
            return "chatroom";
        } else {
            return "forward:/chatroom-not-found.html";
        }
    }

    @GetMapping("media/{chatroomId}/{chatId}")
    public ResponseEntity<Resource> getMediaData(@PathVariable Long chatroomId, @PathVariable Long chatId) throws IOException {
        // 1. 유저 id 획득
        // 2. 유저가 채팅방에 속하는지 확인
        // 3. 채팅 id가 채팅방에 속하는지 확인
        // 4. 반환
        throw new IOException();
    }
}
