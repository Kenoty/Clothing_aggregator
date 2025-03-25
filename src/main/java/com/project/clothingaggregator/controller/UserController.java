package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.dto.UserRegistrationRequest;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.mapper.UserMapper;
import com.project.clothingaggregator.model.UserModel;
import com.project. clothingaggregator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserModel> registerUser(@RequestBody UserRegistrationRequest request) {
        User user = userService.createUser(request);
        UserModel userModel = UserMapper.toModel(user);
        return ResponseEntity.ok(userModel);
    }
}
