package com.project.clothingaggregator.model;

import java.time.LocalDate;

public class UserModel {
    private int id;
    private String name;
    private LocalDate birthday;
    private String email;

    public UserModel(int id, String name, LocalDate birthday, String email) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.email = email;
    }

    public UserModel() {}

    public void setId(int userId) {
        this.id = userId;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String userName) {
        this.name = userName;
    }

    public String getName() {
        return this.name;
    }

    public void setBirthday(LocalDate date) {
        this.birthday = date;
    }

    public LocalDate getBirthday() {
        return this.birthday;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }
}
