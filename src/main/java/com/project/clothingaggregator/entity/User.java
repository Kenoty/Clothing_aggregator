package com.project.clothingaggregator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column
    private LocalDate birthday;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 128)
    private String password;

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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword()  {
        return this.password;
    }
}
