package tqs_hw1.users.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs_hw1.users.model.User;
import tqs_hw1.users.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:8090", allowedHeaders = "*")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Getting all users");
        try {
            List<User> users = userService.getAllUsers();
            logger.info("Retrieved {} users", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error getting users: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        logger.info("Deleting user with ID: {}", userId);
        try {
            if (userService.deleteUser(userId)) {
                logger.info("User {} deleted successfully", userId);
                return ResponseEntity.ok().build();
            }
            logger.warn("User {} not found", userId);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error deleting user {}: ", userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 