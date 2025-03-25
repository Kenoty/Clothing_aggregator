package com.project.clothingaggregator.mapper;

import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.model.UserModel;

public class UserMapper {
    public static UserModel toModel(User user) {
        UserModel model = new UserModel();
        model.setId(user.getId());
        model.setName(user.getName());
        model.setBirthday(user.getBirthday());
        model.setEmail(user.getEmail());
        return model;
    }
}
