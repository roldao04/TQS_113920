package tqs_hw1.users.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tqs_hw1.users.model.User;
import tqs_hw1.users.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        logger.debug("Checking if username exists: {}", username);
        return userRepository.findByUsername(username).isPresent();
    }

    public User findByUsername(String username) {
        logger.debug("Finding user by username: {}", username);
        return userRepository.findByUsername(username).orElse(null);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User not found");
        }
        return userRepository.save(user);
    }
} 