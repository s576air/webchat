package com.s576air.webchat.controller;

import com.s576air.webchat.domain.ChatData;
import com.s576air.webchat.service.ChatService;
import com.s576air.webchat.service.ChatroomService;
import com.s576air.webchat.service.FriendService;
import com.s576air.webchat.service.UsersCache;
import com.s576air.webchat.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
public class ChatroomController {
    private final ChatroomService chatroomService;
    private final ChatService chatService;
    private final FriendService friendService;
    private final UsersCache usersCache;

    @Autowired
    public ChatroomController(ChatroomService chatroomService, ChatService chatService, UsersCache usersCache, FriendService friendService) {
        this.chatroomService = chatroomService;
        this.chatService = chatService;
        this.friendService = friendService;
        this.usersCache = usersCache;
    }

    @GetMapping("chatroom")
    public String chatroomRedirect() {
        return "redirect:/friend-list";
    }

    @PostMapping("chatroom")
    public String chatroomPage(@RequestParam("id") Long chatroomId, Model model) {
        if (chatroomId == null) return "forward:/chatroom-not-found.html";
        Long userId = SecurityUtil.getUserId().orElseThrow();

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
        Long userId = SecurityUtil.getUserId().orElseThrow();

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
    public ResponseEntity<String> upload(@PathVariable Long chatroomId, @RequestParam("file") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("잘못된 파일입니다.");
        }

        String contentType = file.getContentType();
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new IOException();
        }

        Long userId = SecurityUtil.getUserId().orElseThrow();

        ChatData chatData = new ChatData(inputStream, contentType);
        Optional<Long> result = chatService.saveDataChat(chatroomId, userId, chatData);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get().toString());
        } else {
            return ResponseEntity.badRequest().body("저장에 실패했습니다.");
        }
    }

    @GetMapping("chatroom-add")
    public String addChatroom(Model model) {
        Long userId = SecurityUtil.getUserId().orElseThrow();

        Map<Long, String> friendsIdNameMap = friendService.getFriendsIdNameMap(userId);

        model.addAttribute("map", friendsIdNameMap);

        return "chatroom-add";
    }

    @PostMapping("make-chatroom")
    public String makeChatroom(@RequestParam("name") String name, @RequestParam("id") List<Long> ids) {
        if (name == null || ids == null) return "redirect:/chatroom-list";

        Long userId = SecurityUtil.getUserId().orElseThrow();

        Set<Long> friendIds = new HashSet<>(friendService.getFriendIds(userId));

        for (Long id: ids) {
            if (!friendIds.contains(id)) {
                return "redirect:/chatroom-list";
            }
        }
        ids.add(userId);

        chatroomService.insert(name, ids);

        return "redirect:/chatroom-list";
    }
}
