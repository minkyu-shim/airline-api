package com.epita.airlineapi.controller;

import com.epita.airlineapi.model.User;
import com.epita.airlineapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users") // Added leading slash
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getUsers();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(users);
    }

    // GET ONE
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        // No try-catch needed.
        // Service throws NoSuchElementException -> GlobalHandler returns 404
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // CREATE
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // No try-catch needed.
        // Service throws IllegalStateException (if email taken) -> GlobalHandler returns 400/409
        User createdUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // UPDATE
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User userUpdate) {
        // No try-catch needed.
        // Handles 404 (User not found) and 400 (Email taken/Invalid data) automatically
        User updatedUser = userService.updateUser(userId, userUpdate);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        // No try-catch needed.
        // Service throws NoSuchElementException -> GlobalHandler returns 404
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}