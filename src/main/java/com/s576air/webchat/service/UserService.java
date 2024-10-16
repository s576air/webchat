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

    public boolean signUp(String id, String password) {
        if (repository.findById(id).isPresent()) {
            return false;
        }
        User user = new User(id, "", "");
        user.setPassword(password);

        repository.insertUser(user);

        return true;
    }

}
