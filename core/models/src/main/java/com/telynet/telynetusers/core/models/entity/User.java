package com.telynet.telynetusers.core.models.entity;

public class User {
    private final String name;
    private final String email;

    private final String code;

    private final String phone;

    private final boolean isVisited;

    public User(String code, String name, String email, String phone, boolean isVisited) {
        this.name = name;
        this.email = email;
        this.code = code;
        this.phone = phone;
        this.isVisited = isVisited;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isVisited() {
        return isVisited;
    }
}
