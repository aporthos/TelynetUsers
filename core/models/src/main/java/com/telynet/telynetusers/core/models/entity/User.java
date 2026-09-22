package com.telynet.telynetusers.core.models.entity;

public class User {
    private final String code;
    private final String name;
    private final String email;
    private final String phone;
    private final boolean isVisited;
    private final String address;
    private final String imageUrl;
    private final String company;


    public User(String code, String name, String email, String phone, boolean isVisited, String address, String imageUrl, String company) {
        this.name = name;
        this.email = email;
        this.code = code;
        this.phone = phone;
        this.isVisited = isVisited;
        this.address = address;
        this.imageUrl = imageUrl;
        this.company = company;
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

    public String getAddress() {
        return address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCompany() {
        return company;
    }
}
