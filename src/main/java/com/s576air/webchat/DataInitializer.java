package com.s576air.webchat;

import com.s576air.webchat.domain.User;
import com.s576air.webchat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    @Override
    public void run(String... args) throws Exception {
        if (repository.findById("test").isEmpty()) {
            User user = new User("test", "", "name");
            user.setPassword("password");
            repository.insertUser(user);
        }
    }
}
