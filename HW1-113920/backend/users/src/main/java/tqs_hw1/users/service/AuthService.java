package tqs_hw1.users.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs_hw1.users.dto.AuthResponse;
import tqs_hw1.users.dto.LoginRequest;
import tqs_hw1.users.dto.RegisterRequest;
import tqs_hw1.users.model.User;
import tqs_hw1.users.repository.UserRepository;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    public AuthResponse login(LoginRequest request) {
        logger.info("Processing login request for user: {}", request.getUsername());
        return userRepository.findByUsername(request.getUsername())
                .filter(user -> user.getPassword().equals(request.getPassword()))
                .map(user -> {
                    logger.info("Login successful for user: {}", request.getUsername());
                    return new AuthResponse(true, user.getRole(), "Login successful");
                })
                .orElseGet(() -> {
                    logger.warn("Login failed for user: {}", request.getUsername());
                    return new AuthResponse(false, null, "Invalid credentials");
                });
    }

    public AuthResponse register(RegisterRequest request) {
        logger.info("Processing registration request for user: {}", request.getUsername());
        
        try {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                logger.warn("Username already exists: {}", request.getUsername());
                return new AuthResponse(false, null, "Username already exists");
            }

            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());
            user.setName(request.getName());
            user.setRole(request.getRole());

            logger.debug("Saving new user: {}", user);
            user = userRepository.save(user);
            logger.info("User registered successfully: {}", user.getUsername());
            
            return new AuthResponse(true, user.getRole(), "Registration successful");
        } catch (Exception e) {
            logger.error("Error during registration: ", e);
            return new AuthResponse(false, null, "Registration failed: " + e.getMessage());
        }
    }
} 