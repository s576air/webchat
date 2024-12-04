package com.s576air.webchat.domain;

import com.s576air.webchat.util.PasswordUtil;

public class User {
    Long id;
    String loginId;
    String passwordHash;
    String name;

    public User(Long id, String loginId, String passwordHash, String name) {
        this.id = id;
        this.loginId = loginId;
        this.passwordHash = passwordHash;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPassword(String password) {
        passwordHash = PasswordUtil.hashPassword(password);
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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
