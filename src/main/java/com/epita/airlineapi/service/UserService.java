package com.epita.airlineapi.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void insertUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        boolean exists = userRepository.existsById(id);

        if (!exists) {
            throw new IllegalStateException("user with id " + id + "does not exist");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUser(Integer userId, String firstName, String email) {
        // checking if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("user with id " + userId + "does not exist"));

        // validate and update first name
        if (firstName != null &&
                firstName.length() > 0 &&
                !Objects.equals(user.getFirstName(), firstName)) {

            user.setFirstName(firstName);
        }
    }
}
