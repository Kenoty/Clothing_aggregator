package com.project.clothingaggregator.service;

import com.project.clothingaggregator.dto.UserRegistrationRequest;
import com.project.clothingaggregator.entity.User;
import com.project.clothingaggregator.model.UserModel;
import com.project.clothingaggregator.repository.UserRepository;
import com.project.clothingaggregator.security.PasswordUtil;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordUtil passwordUtil;
//    private final Map<Integer, UserModel> userDatabase = new HashMap<>();
//
//    public UserService() {
//        initializeUsers();
//    }
//
//    private void initializeUsers() {
//        userDatabase.put(1, new UserModel(1, "Igor", LocalDate.of(1990, 1, 1), "igor@example.com"));
//        userDatabase.put(2, new UserModel(2, "Bob", LocalDate.of(1985, 5, 15), "bob@mail.ru"));
//        userDatabase.put(3, new UserModel(3, "John", LocalDate.of(2004, 7, 25), "john@gmail.com"));
//        userDatabase.put(4, new UserModel(4, "Igor", LocalDate.of(2000, 3, 18), "example@example.com"));
//    }
//
//    public List<UserModel> findByName(String name) {
//        return userDatabase.values().stream()
//                .filter(u -> u.getName().equalsIgnoreCase(name))
//                .collect(Collectors.toList());
//    }
//
//    public Optional<UserModel> getUserById(int id) {
//        return Optional.ofNullable(userDatabase.get(id));
//    }

    public User createUser(UserRegistrationRequest request) {
        User user = new User();
        user.setName(request.getUsername());
        user.setBirthday(request.getBirthday());
        user.setEmail(request.getEmail());
        user.setPassword(passwordUtil.hashPassword(request.getPassword()));
        return userRepository.save(user);
    }
}