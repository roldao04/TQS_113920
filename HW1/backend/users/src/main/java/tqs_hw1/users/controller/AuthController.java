package tqs_hw1.users.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import tqs_hw1.users.dto.LoginRequest;
import tqs_hw1.users.dto.RegisterRequest;
import tqs_hw1.users.dto.AuthResponse;
import tqs_hw1.users.service.AuthService;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:8090", allowedHeaders = "*")
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostConstruct
    public void init() {
        logger.info("AuthController initialized");
    }

    @Operation(summary = "Test endpoint", description = "Simple test endpoint to verify the controller is working")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Test successful",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/test")
    public String test() {
        logger.info("Test endpoint called");
        return "Auth controller test endpoint!";
    }

    @Operation(summary = "User login", description = "Authenticates a user with username and password")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Login credentials", required = true,
                content = @Content(schema = @Schema(implementation = LoginRequest.class)))
            @RequestBody LoginRequest request) {
        logger.info("Login request received for user: {}", request.getUsername());
        logger.debug("Login request details: {}", request);
        
        try {
            AuthResponse response = authService.login(request);
            logger.info("Login response for user {}: success={}", request.getUsername(), response.isSuccess());
            logger.debug("Full login response: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during login for user {}: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "User registration", description = "Registers a new user in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registration successful",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid registration data"),
        @ApiResponse(responseCode = "409", description = "Username already exists"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Registration details", required = true,
                content = @Content(schema = @Schema(implementation = RegisterRequest.class)))
            @RequestBody RegisterRequest request) {
        logger.info("Register request received for user: {}", request.getUsername());
        logger.debug("Register request details: {}", request);
        
        try {
            AuthResponse response = authService.register(request);
            logger.info("Registration response for user {}: success={}", request.getUsername(), response.isSuccess());
            logger.debug("Full registration response: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error during registration for user {}: {}", request.getUsername(), e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
} 