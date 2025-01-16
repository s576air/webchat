package com.s576air.webchat.security;

import com.s576air.webchat.domain.CustomUserDetails;
import com.s576air.webchat.service.ChatroomsCache;
import com.s576air.webchat.service.UserService;
import com.s576air.webchat.service.UsersCache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final UserService userService;
    private final UsersCache usersCache;
    private final ChatroomsCache chatroomsCache;

    @Autowired
    public CustomLogoutSuccessHandler(UserService userService, UsersCache usersCache, ChatroomsCache chatroomsCache) {
        this.userService = userService;
        this.usersCache = usersCache;
        this.chatroomsCache = chatroomsCache;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        Optional<List<Long>> usedChatroomIds = usersCache.getUsedChatroomIds(userId);
        if (usedChatroomIds.isPresent()) {
            for (Long chatroomId : usedChatroomIds.get()) {
                chatroomsCache.removeChatroomIfIdle(chatroomId);
            }
        }
        userService.removeCacheUser(userId);
    }
}
