package tqs_hw1.users.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs_hw1.users.dto.AuthResponse;
import tqs_hw1.users.dto.LoginRequest;
import tqs_hw1.users.dto.RegisterRequest;
import tqs_hw1.users.model.User;
import tqs_hw1.users.model.UserRole;
import tqs_hw1.users.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private User user1;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setEmail("user1@example.com");
        user1.setName("User One");
        user1.setRole(UserRole.USER);

        loginRequest = new LoginRequest();
        loginRequest.setUsername("user1");
        loginRequest.setPassword("password1");

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setPassword("newpassword");
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setName("New User");
        registerRequest.setRole(UserRole.USER);
    }

    @Test
    void whenLogin_withValidCredentials_thenReturnSuccessResponse() {
        // Arrange
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertThat(response)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.isSuccess()).isTrue();
                assertThat(r.getRole()).isEqualTo(UserRole.USER);
                assertThat(r.getMessage()).isEqualTo("Login successful");
            });
        verify(userRepository).findByUsername("user1");
    }

    @Test
    void whenLogin_withInvalidUsername_thenReturnFailureResponse() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
        loginRequest.setUsername("nonexistent");

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertThat(response)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.isSuccess()).isFalse();
                assertThat(r.getRole()).isNull();
                assertThat(r.getMessage()).isEqualTo("Invalid credentials");
            });
        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    void whenLogin_withInvalidPassword_thenReturnFailureResponse() {
        // Arrange
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));
        loginRequest.setPassword("wrongpassword");

        // Act
        AuthResponse response = authService.login(loginRequest);

        // Assert
        assertThat(response)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.isSuccess()).isFalse();
                assertThat(r.getRole()).isNull();
                assertThat(r.getMessage()).isEqualTo("Invalid credentials");
            });
        verify(userRepository).findByUsername("user1");
    }

    @Test
    void whenRegister_withNewUsername_thenReturnSuccessResponse() {
        // Arrange
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User savedUser = i.getArgument(0);
            savedUser.setId(2L);
            return savedUser;
        });

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertThat(response)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.isSuccess()).isTrue();
                assertThat(r.getRole()).isEqualTo(UserRole.USER);
                assertThat(r.getMessage()).isEqualTo("Registration successful");
            });
        verify(userRepository).findByUsername("newuser");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void whenRegister_withExistingUsername_thenReturnFailureResponse() {
        // Arrange
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));
        registerRequest.setUsername("user1");

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertThat(response)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.isSuccess()).isFalse();
                assertThat(r.getRole()).isNull();
                assertThat(r.getMessage()).isEqualTo("Username already exists");
            });
        verify(userRepository).findByUsername("user1");
        verify(userRepository, never()).save(any());
    }

    @Test
    void whenRegister_withException_thenReturnFailureResponse() {
        // Arrange
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class)))
            .thenThrow(new RuntimeException("Database error"));

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertThat(response)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.isSuccess()).isFalse();
                assertThat(r.getRole()).isNull();
                assertThat(r.getMessage()).contains("Registration failed");
            });
        verify(userRepository).findByUsername("newuser");
        verify(userRepository).save(any(User.class));
    }
} 