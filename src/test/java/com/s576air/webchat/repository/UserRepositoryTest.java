package com.s576air.webchat.repository;


import com.s576air.webchat.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Optional;

public class UserRepositoryTest {
    JdbcTemplate jdbcTemplate;
    UserRepository repository;

    public UserRepositoryTest() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        jdbcTemplate = new JdbcTemplate(dataSource);
        repository = new UserRepository(jdbcTemplate);
    }

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute(
            "CREATE TABLE IF NOT EXISTS users (" +
            "    id VARCHAR(16) PRIMARY KEY," +
            "    password_hash VARCHAR(255)," +
            "    name VARCHAR(255)" +
            ");"
        );

        repository.clear();
    }

    @Test
    public void test() {
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
