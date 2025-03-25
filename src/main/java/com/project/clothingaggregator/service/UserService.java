package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.UserRegistrationRequest;
import com.project.clothingaggregator.dto.UserUpdateRequest;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.exception.NotFoundException;
import com.project.clothingaggregator.model.UserModel;
import com.project.clothingaggregator.repository.UserRepository;
import com.project.clothingaggregator.security.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordUtil passwordUtil;

    public User createUser(UserRegistrationRequest request) {
        User user = new User();
        user.setName(request.getUsername());
        user.setBirthday(request.getBirthday());
        user.setEmail(request.getEmail());
        user.setPassword(passwordUtil.hashPassword(request.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Integer id, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setName(updateRequest.getUsername());
        user.setBirthday(updateRequest.getBirthday());
        user.setEmail(updateRequest.getEmail());

        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}