package com.s576air.webchat.util;

import com.s576air.webchat.domain.CustomUserDetails;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtil {
    private SecurityUtil() {}

    public static boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null &&
            authentication.isAuthenticated() &&
            !(authentication instanceof AnonymousAuthenticationToken);
    }

    public static Optional<Long> getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Long id = userDetails.getId();

        return Optional.of(id);
    }
}
