package tqs_hw1.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Admin", description = "Admin management APIs")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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

    @Operation(summary = "Delete user", description = "Deletes a specific user by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User successfully deleted"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin access required"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "ID of the user to delete") @PathVariable Long userId) {
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