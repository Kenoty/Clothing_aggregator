package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.dto.UserRegistrationRequest;
import com.project.clothingaggregator.dto.UserUpdateRequest;
import com.project.clothingaggregator.dto.UserWithOrdersDto;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.mapper.UserMapper;
import com.project.clothingaggregator.model.UserModel;
import com.project. clothingaggregator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
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

    @PutMapping("/{id}")
    public ResponseEntity<UserModel> updateUser(
            @PathVariable Integer id,
            @RequestBody UserUpdateRequest updateRequest) {
        User updatedUser = userService.updateUser(id, updateRequest);
        UserModel userModel = UserMapper.toModel(updatedUser);
        return ResponseEntity.ok(userModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        UserModel userModel = UserMapper.toModel(user);
        return ResponseEntity.ok(userModel);
    }

    @GetMapping("/all")
    public  PagedModel<EntityModel<UserWithOrdersDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<UserWithOrdersDto> assembler) {
        return assembler.toModel(userService
                .getAllUsersWithOrdersAndProducts(page, size));
    }
}
