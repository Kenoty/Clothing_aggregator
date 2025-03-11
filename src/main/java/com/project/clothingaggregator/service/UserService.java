package com.project.clothingaggregator.service;

import com.project.clothingaggregator.model.User;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final Map<Integer, User> userDatabase = new HashMap<>();

    public UserService() {
        initializeUsers();
    }

    private void initializeUsers() {
        userDatabase.put(1, new User(1, "Igor", LocalDate.of(1990, 1, 1), "igor@example.com"));
        userDatabase.put(2, new User(2, "Bob", LocalDate.of(1985, 5, 15), "bob@mail.ru"));
        userDatabase.put(3, new User(3, "John", LocalDate.of(2004, 7, 25), "john@gmail.com"));
        userDatabase.put(4, new User(4, "Igor", LocalDate.of(2000, 3, 18), "example@example.com"));
    }

    public List<User> findByName(String name) {
        return userDatabase.values().stream()
                .filter(u -> u.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(userDatabase.get(id));
    }
}