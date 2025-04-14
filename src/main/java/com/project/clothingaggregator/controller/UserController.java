package com.project.clothingaggregator.controller;

import com.project.clothingaggregator.dto.UserDto;
import com.project.clothingaggregator.dto.UserRegistrationRequest;
import com.project.clothingaggregator.dto.UserUpdateRequest;
import com.project.clothingaggregator.dto.UserWithOrdersDto;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.mapper.UserMapper;
import com.project. clothingaggregator.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserRegistrationRequest request) {
        User user = userService.createUser(request);
        UserDto userDto = UserMapper.toModel(user);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Integer id,
            @RequestBody UserUpdateRequest updateRequest) {
        User updatedUser = userService.updateUser(id, updateRequest);
        UserDto userDto = UserMapper.toModel(updatedUser);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        UserDto userDto = UserMapper.toModel(user);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/all")
    public  ResponseEntity<List<UserWithOrdersDto>> getAll() {
        return ResponseEntity.ok(userService.getAllUsersWithOrdersAndProducts());
    }
}
