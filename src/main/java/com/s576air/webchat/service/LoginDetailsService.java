package com.s576air.webchat.service;

import com.s576air.webchat.domain.CustomUserDetails;
import com.s576air.webchat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class LoginDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.s576air.webchat.domain.User user = userRepository.findByLoginId(username)
                              .orElseThrow(() -> new UsernameNotFoundException("User (" + username + ") not found"));

        return new CustomUserDetails(
            user.getId(), // Database 기본키인 id
            user.getLoginId(),
            user.getPasswordHash(),
            Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );
    }
}
