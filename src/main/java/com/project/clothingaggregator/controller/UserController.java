package com.project.clothingaggregator.controller;

import com.project. clothingaggregator.model.User;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final Map<Integer, User> userDatabase = new HashMap<>();

    public UserController() {
        userDatabase.put(1, new User(1, "Igor", LocalDate.of(1990, 1, 1), "igor@example.com"));
        userDatabase.put(2, new User(2, "Bob", LocalDate.of(1985, 5, 15), "bob@mail.ru"));
        userDatabase.put(3, new User(3, "John", LocalDate.of(2004, 7, 25), "john@gmail.com"));
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserInfoWithQueryParam(@RequestParam(value = "id") int id) {
        User user = userDatabase.get(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserInfoWithPathParam(@PathVariable int id) {
        User user = userDatabase.get(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(user);
    }
}
