package tqs_hw1.users.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tqs_hw1.users.dto.AuthResponse;
import tqs_hw1.users.dto.LoginRequest;
import tqs_hw1.users.dto.RegisterRequest;
import tqs_hw1.users.model.UserRole;
import tqs_hw1.users.service.AuthService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private AuthResponse successResponse;
    private AuthResponse failureResponse;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setPassword("newpassword");
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setName("New User");
        registerRequest.setRole(UserRole.USER);

        successResponse = new AuthResponse(true, UserRole.USER, "Operation successful");
        failureResponse = new AuthResponse(false, null, "Operation failed");
    }

    @Test
    void whenTestEndpoint_thenReturnTestMessage() {
        // Act
        String result = authController.test();

        // Assert
        assertThat(result).isEqualTo("Auth controller test endpoint!");
    }

    @Test
    void whenLogin_withValidCredentials_thenReturnSuccessResponse() {
        // Arrange
        when(authService.login(any(LoginRequest.class))).thenReturn(successResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.isSuccess()).isTrue();
                assertThat(r.getRole()).isEqualTo(UserRole.USER);
                assertThat(r.getMessage()).isEqualTo("Operation successful");
            });
        verify(authService).login(loginRequest);
    }

    @Test
    void whenLogin_withInvalidCredentials_thenReturnSuccessResponseWithFailure() {
        // Arrange
        when(authService.login(any(LoginRequest.class))).thenReturn(failureResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.isSuccess()).isFalse();
                assertThat(r.getRole()).isNull();
                assertThat(r.getMessage()).isEqualTo("Operation failed");
            });
        verify(authService).login(loginRequest);
    }

    @Test
    void whenLogin_withException_thenReturnInternalServerError() {
        // Arrange
        when(authService.login(any(LoginRequest.class)))
            .thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<AuthResponse> response = authController.login(loginRequest);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull();
        verify(authService).login(loginRequest);
    }

    @Test
    void whenRegister_withValidData_thenReturnSuccessResponse() {
        // Arrange
        when(authService.register(any(RegisterRequest.class))).thenReturn(successResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.register(registerRequest);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.isSuccess()).isTrue();
                assertThat(r.getRole()).isEqualTo(UserRole.USER);
                assertThat(r.getMessage()).isEqualTo("Operation successful");
            });
        verify(authService).register(registerRequest);
    }

    @Test
    void whenRegister_withExistingUsername_thenReturnSuccessResponseWithFailure() {
        // Arrange
        when(authService.register(any(RegisterRequest.class))).thenReturn(failureResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.register(registerRequest);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.isSuccess()).isFalse();
                assertThat(r.getRole()).isNull();
                assertThat(r.getMessage()).isEqualTo("Operation failed");
            });
        verify(authService).register(registerRequest);
    }

    @Test
    void whenRegister_withException_thenReturnInternalServerError() {
        // Arrange
        when(authService.register(any(RegisterRequest.class)))
            .thenThrow(new RuntimeException("Unexpected error"));

        // Act
        ResponseEntity<AuthResponse> response = authController.register(registerRequest);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull();
        verify(authService).register(registerRequest);
    }
} 