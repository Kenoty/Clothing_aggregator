package com.project.clothingaggregator.model;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserModel {
    private int id;
    private String username;
    private LocalDate birthday;
    private String email;

    public UserModel(int id, String name, LocalDate birthday, String email) {
        this.id = id;
        this.username = name;
        this.birthday = birthday;
        this.email = email;
    }

    public UserModel() {}
}
