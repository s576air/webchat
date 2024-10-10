package com.s576air.webchat.service;

import com.s576air.webchat.domain.User;
import com.s576air.webchat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void login(String id, String password) {
        // SQL 인젝션 공격이 가능한지 확인바람
        Optional<User> optionalUser = repository.findById(id);

        if (optionalUser.isEmpty()) {
            return;
        }

        User user = optionalUser.get();

        if (PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            return;
        } else {
            return;
        }
    }
}
