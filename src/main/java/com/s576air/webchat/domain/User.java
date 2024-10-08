package com.s576air.webchat.domain;

import com.s576air.webchat.service.PasswordUtil;

public class User {
    String id;
    String passwordHash;
    String name;

    public User(String id, String passwordHash, String name) {
        this.id = id;
        this.passwordHash = passwordHash;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) {
        passwordHash = PasswordUtil.hashPassword(password);
    }

    public boolean checkPassword(String password) {
        return PasswordUtil.checkPassword(password, this.passwordHash);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id.equals(user.id) && passwordHash.equals(user.passwordHash) && name.equals(user.name);
    }

}
