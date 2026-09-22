package com.telynet.telynetusers.core.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @NonNull
    @PrimaryKey
    private String code = "";
    private String name;
    private String email;
    private String phone;
    private boolean isVisited;

    private String address;
    private String imageUrl;
    private String company;
    private boolean isFavorite;

    public UserEntity() {
    }

    public UserEntity(@NonNull String code, String name, String email, String phone, boolean isVisited, String address, String imageUrl, String company, boolean isFavorite) {
        this.name = name;
        this.email = email;
        this.code = code;
        this.phone = phone;
        this.isVisited = isVisited;
        this.address = address;
        this.imageUrl = imageUrl;
        this.company = company;
        this.isFavorite = isFavorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NonNull
    public String getCode() {
        return code;
    }

    public void setCode(@NonNull String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
