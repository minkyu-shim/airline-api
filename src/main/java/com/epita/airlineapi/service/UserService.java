package com.epita.airlineapi.service;

import com.epita.airlineapi.model.User;
import com.epita.airlineapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public User saveUser(User user) {
        // Concrete Example: Check if email is taken before saving
        Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
        if (userOptional.isPresent()) {
            throw new IllegalStateException("Email taken");
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("User with id " + id + " does not exist");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUser(Long userId, User updateRequest) {
        // 1. Retrieve the existing user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with id " + userId + " does not exist"));

        // 2. Update First Name
        if (updateRequest.getFirstName() != null &&
                !updateRequest.getFirstName().isEmpty() &&
                !Objects.equals(user.getFirstName(), updateRequest.getFirstName())) {
            user.setFirstName(updateRequest.getFirstName());
        }

        // 3. Update Last Name
        if (updateRequest.getLastName() != null &&
                !updateRequest.getLastName().isEmpty() &&
                !Objects.equals(user.getLastName(), updateRequest.getLastName())) {
            user.setLastName(updateRequest.getLastName());
        }

        // 4. Update Email (With validation)
        if (updateRequest.getEmail() != null &&
                !updateRequest.getEmail().isEmpty() &&
                !Objects.equals(user.getEmail(), updateRequest.getEmail())) {

            Optional<User> userOptional = userRepository.findUserByEmail(updateRequest.getEmail());
            if (userOptional.isPresent()) {
                throw new IllegalStateException("Email taken");
            }
            user.setEmail(updateRequest.getEmail());
        }

        // 5. Update Address (Address can be optional/nullable, so we might not check isEmpty)
        if (updateRequest.getAddress() != null &&
                !Objects.equals(user.getAddress(), updateRequest.getAddress())) {
            user.setAddress(updateRequest.getAddress());
        }

        // 6. Update Phone Number
        if (updateRequest.getPhoneNumber() != null &&
                !Objects.equals(user.getPhoneNumber(), updateRequest.getPhoneNumber())) {
            user.setPhoneNumber(updateRequest.getPhoneNumber());
        }

        // 7. Update Birth Date
        if (updateRequest.getBirthDate() != null &&
                !Objects.equals(user.getBirthDate(), updateRequest.getBirthDate())) {
            user.setBirthDate(updateRequest.getBirthDate());
        }
    }
}