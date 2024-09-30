package com.s576air.webchat.repository;


import org.junit.jupiter.api.AfterEach;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserRepositoryTest {
    UserRepository repository = new UserRepository(new JdbcTemplate());

    @AfterEach
    public void afterEach() {
        repository.clear();
    }
}
