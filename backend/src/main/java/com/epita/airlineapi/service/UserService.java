package com.epita.airlineapi.service;

import com.epita.airlineapi.model.User;
import com.epita.airlineapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException; // Standard for 404
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // GET ALL
    // Note: Because of JOINED inheritance, this returns Users, Clients, AND Employees.
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // GET ONE
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                // CHANGED: IllegalStateException -> NoSuchElementException for 404 behavior
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " does not exist"));
    }

    // CREATE
    // ⚠️ CAUTION: Only use this for generic Users (Admins), not Clients/Employees.
    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            // IllegalStateException is acceptable here for 409 Conflict or 400 Bad Request
            throw new IllegalStateException("Email " + user.getEmail() + " is already taken");
        }
        return userRepository.save(user);
    }

    // DELETE
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            // CHANGED: IllegalStateException -> NoSuchElementException
            throw new NoSuchElementException("User with id " + id + " does not exist");
        }
        userRepository.deleteById(id);
    }

    // UPDATE
    @Transactional
    public User updateUser(Long userId, User updateRequest) {
        User user = getUserById(userId); // Re-uses the 404 check above

        // 1.Common Fields Update
        if (updateRequest.getFirstName() != null && !updateRequest.getFirstName().isEmpty() &&
                !Objects.equals(user.getFirstName(), updateRequest.getFirstName())) {
            user.setFirstName(updateRequest.getFirstName());
        }

        if (updateRequest.getLastName() != null && !updateRequest.getLastName().isEmpty() &&
                !Objects.equals(user.getLastName(), updateRequest.getLastName())) {
            user.setLastName(updateRequest.getLastName());
        }

        // 2.Email Update (Smart Check)
        if (updateRequest.getEmail() != null && !updateRequest.getEmail().isEmpty() &&
                !Objects.equals(user.getEmail(), updateRequest.getEmail())) {

            // Only check DB if the email is actually changing
            if (userRepository.existsByEmail(updateRequest.getEmail())) {
                throw new IllegalStateException("Email " + updateRequest.getEmail() + " is already taken");
            }
            user.setEmail(updateRequest.getEmail());
        }

        // 3.Other fields
        if (updateRequest.getAddress() != null &&
                !Objects.equals(user.getAddress(), updateRequest.getAddress())) {
            user.setAddress(updateRequest.getAddress());
        }

        if (updateRequest.getPhoneNumber() != null &&
                !Objects.equals(user.getPhoneNumber(), updateRequest.getPhoneNumber())) {
            user.setPhoneNumber(updateRequest.getPhoneNumber());
        }

        if (updateRequest.getBirthDate() != null &&
                !Objects.equals(user.getBirthDate(), updateRequest.getBirthDate())) {
            user.setBirthDate(updateRequest.getBirthDate());
        }

        return user; // Dirty checking handles the save
    }
}