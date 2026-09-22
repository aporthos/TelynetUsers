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

    public UserEntity() {
    }

    public UserEntity(@NonNull String code, String name, String email, String phone, boolean isVisited) {
        this.name = name;
        this.email = email;
        this.code = code;
        this.phone = phone;
        this.isVisited = isVisited;
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
}
