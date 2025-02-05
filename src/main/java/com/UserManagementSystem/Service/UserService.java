package com.UserManagementSystem.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.UserManagementSystem.Entity.User;
import com.UserManagementSystem.Repository.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        logger.info("Registering user with email: {}", user.getEmail());
        try {
            // Hash password before saving
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Error registering user with email: {}", user.getEmail(), e);
            throw e;
        }
    }

    public User loginUser(String email, String password) {
        logger.info("Attempting login for user with email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("User with email {} not found", email);
                    return new RuntimeException("User not found");
                });
        if (!user.getPassword().equals(password)) {
            logger.warn("Invalid credentials for user with email: {}", email);
            throw new RuntimeException("Invalid credentials");
        }
        logger.info("Login successful for user with email: {}", email);
        return user;
    }

    public User updateUser(Long id, User updatedUser) {
        logger.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User with ID {} not found", id);
                    return new RuntimeException("User not found");
                });
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword()); // Ideally rehash this
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        logger.info("Fetching user with ID: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User with ID {} not found", id);
                    return new RuntimeException("User not found");
                });
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User with ID {} not found", id);
                    return new RuntimeException("User not found");
                });
        userRepository.delete(user);
        logger.info("User with ID {} deleted successfully", id);
    }
}


