package com.s576air.webchat.controller;

import com.s576air.webchat.domain.ChatData;
import com.s576air.webchat.domain.CustomUserDetails;
import com.s576air.webchat.service.ChatService;
import com.s576air.webchat.service.ChatroomService;
import com.s576air.webchat.service.UsersCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        if (!chatroomService.containsUser(chatroomId, userId)) throw new IOException();

        if (!chatService.chatroomContainsChat(chatroomId, chatId)) throw new IOException();

        Optional<ChatData> optionalChatData = chatService.getChatData(chatId);

        if (optionalChatData.isEmpty()) throw new IOException();

        ChatData chatData = optionalChatData.get();

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(chatData.getContentType()))
            .body(new InputStreamResource(chatData.getData()));
    }

    @PostMapping("upload/{chatroomId}")
    public ResponseEntity<String> upload(@PathVariable Long chatroomId, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("파일이 비어있습니다.");
        }

        //

        return ResponseEntity.ok("아직 구현 안됨");
    }
}
