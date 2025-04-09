package tqs_hw1.users.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs_hw1.users.model.User;
import tqs_hw1.users.repository.UserRepository;
import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        logger.debug("Getting all users");
        return userRepository.findAll();
    }

    public boolean deleteUser(Long userId) {
        logger.debug("Attempting to delete user with ID: {}", userId);
        if (!userRepository.existsById(userId)) {
            logger.warn("User with ID {} not found", userId);
            return false;
        }
        try {
            userRepository.deleteById(userId);
            logger.info("User with ID {} deleted successfully", userId);
            return true;
        } catch (Exception e) {
            logger.error("Error deleting user with ID {}: ", userId, e);
            throw e;
        }
    }

    public User createUser(User user) {
        logger.debug("Creating new user: {}", user.getUsername());
        try {
            User savedUser = userRepository.save(user);
            logger.info("User {} created successfully", savedUser.getUsername());
            return savedUser;
        } catch (Exception e) {
            logger.error("Error creating user {}: ", user.getUsername(), e);
            throw e;
        }
    }

    public boolean existsByUsername(String username) {
        logger.debug("Checking if username exists: {}", username);
        return userRepository.findByUsername(username).isPresent();
    }

    public User findByUsername(String username) {
        logger.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username).orElse(null);
    }
} 