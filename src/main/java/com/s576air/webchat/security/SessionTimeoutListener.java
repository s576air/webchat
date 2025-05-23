package com.s576air.webchat.security;

import com.s576air.webchat.domain.CustomUserDetails;
import com.s576air.webchat.service.ChatroomsCache;
import com.s576air.webchat.service.UserService;
import com.s576air.webchat.service.UsersCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SessionTimeoutListener implements ApplicationListener<SessionDestroyedEvent> {
    private final UserService userService;
    private final UsersCache usersCache;
    private final ChatroomsCache chatroomsCache;

    @Autowired
    public SessionTimeoutListener(UserService userService, UsersCache usersCache, ChatroomsCache chatroomsCache) {
        this.userService = userService;
        this.usersCache = usersCache;
        this.chatroomsCache = chatroomsCache;
    }

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        event.getSecurityContexts().forEach(context -> {
            CustomUserDetails userDetails = (CustomUserDetails) context.getAuthentication().getPrincipal();
            Long userId = userDetails.getId();

            Optional<List<Long>> usedChatroomIds = usersCache.getUsedChatroomIds(userId);
            if (usedChatroomIds.isPresent()) {
                for (Long chatroomId : usedChatroomIds.get()) {
                    chatroomsCache.removeChatroomIfIdle(chatroomId);
                }
            }
            userService.removeCacheUser(userId);
        });
    }
}