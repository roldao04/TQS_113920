package tqs_hw1.users.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tqs_hw1.users.model.User;
import tqs_hw1.users.model.UserRole;
import tqs_hw1.users.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    private User user1;
    private User user2;
    private List<User> users;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setEmail("user1@example.com");
        user1.setName("User One");
        user1.setRole(UserRole.USER);

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setEmail("user2@example.com");
        user2.setName("User Two");
        user2.setRole(UserRole.STAFF);

        users = Arrays.asList(user1, user2);
    }

    @Test
    void whenGetAllUsers_thenReturnAllUsers() {
        // Arrange
        when(userService.getAllUsers()).thenReturn(users);

        // Act
        ResponseEntity<List<User>> response = adminController.getAllUsers();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
            .isNotNull()
            .hasSize(2)
            .containsExactly(user1, user2);
        verify(userService).getAllUsers();
    }

    @Test
    void whenGetAllUsers_withException_thenReturnInternalServerError() {
        // Arrange
        when(userService.getAllUsers()).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<List<User>> response = adminController.getAllUsers();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull();
        verify(userService).getAllUsers();
    }

    @Test
    void whenDeleteUser_withExistingId_thenReturnOk() {
        // Arrange
        when(userService.deleteUser(1L)).thenReturn(true);

        // Act
        ResponseEntity<?> response = adminController.deleteUser(1L);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).deleteUser(1L);
    }

    @Test
    void whenDeleteUser_withNonExistingId_thenReturnNotFound() {
        // Arrange
        when(userService.deleteUser(99L)).thenReturn(false);

        // Act
        ResponseEntity<?> response = adminController.deleteUser(99L);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userService).deleteUser(99L);
    }

    @Test
    void whenDeleteUser_withException_thenReturnInternalServerError() {
        // Arrange
        when(userService.deleteUser(1L)).thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseEntity<?> response = adminController.deleteUser(1L);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(userService).deleteUser(1L);
    }
} 