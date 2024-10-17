package com.s576air.webchat.service;

import com.s576air.webchat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.s576air.webchat.domain.User user = repository.findById(username)
                              .orElseThrow(() -> new UsernameNotFoundException("User (" + username + ") not found"));

        return User
            .withUsername(user.getId())
            .password(user.getPasswordHash())
            .roles("USER")
            .build();
    }
}
