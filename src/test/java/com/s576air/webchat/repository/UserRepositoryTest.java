package com.s576air.webchat.repository;


import com.s576air.webchat.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.swing.text.html.Option;
import java.util.Optional;

public class UserRepositoryTest {
    UserRepository repository = new UserRepository(new JdbcTemplate());

    @AfterEach
    public void afterEach() {
        repository.clear();
    }

    @Test
    public void test() {
        repository.clear();

        User user1 = new User("first", "hashcode", "name");
        User user2 = new User("second", "password", "java");

        Optional<User> nullUser1 = repository.findById(user1.getId());
        Assertions.assertEquals(Optional.empty(), nullUser1);

        repository.insertUser(user1);
        repository.insertUser(user2);

        Optional<User> repositoryUser1 = repository.findById(user1.getId());
        Assertions.assertEquals(Optional.of(user1), repositoryUser1);

        Optional<User> repositoryUser2 = repository.findById(user2.getId());
        Assertions.assertEquals(Optional.of(user2), repositoryUser2);

        repository.deleteUser(user1.getId());

        Optional<User> deletedUser1 = repository.findById(user1.getId());
        Assertions.assertEquals(Optional.empty(), deletedUser1);
    }
}
