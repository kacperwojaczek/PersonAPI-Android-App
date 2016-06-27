package com.example.kacper.zaliczenie.Models;

import java.util.UUID;

/**
 * Created by Kacper on 18-Jun-16.
 */
public class User {

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.uuid = UUID.randomUUID().toString();
    }

    public User() {}

    private String username;
    private String password;
    private String uuid;
}
