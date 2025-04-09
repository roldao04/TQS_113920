package tqs_hw1.users.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import tqs_hw1.users.model.User;
import tqs_hw1.users.model.UserRole;
import tqs_hw1.users.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        userRepository.deleteAll();

        // Create test users
        user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setEmail("user1@example.com");
        user1.setName("User One");
        user1.setRole(UserRole.USER);

        user2 = new User();
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setEmail("user2@example.com");
        user2.setName("User Two");
        user2.setRole(UserRole.STAFF);

        // Save users to the database
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
    }

    @Test
    void whenCreateUser_withValidData_thenUserIsCreated() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("password");
        newUser.setEmail("newuser@example.com");
        newUser.setName("New User");
        newUser.setRole(UserRole.USER);

        // Act
        User createdUser = userService.createUser(newUser);

        // Assert
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo("newuser");
        assertThat(createdUser.getEmail()).isEqualTo("newuser@example.com");
        assertThat(createdUser.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void whenCreateUser_withExistingUsername_thenThrowException() {
        // Arrange
        User newUser = new User();
        newUser.setUsername("user1"); // Same username as existing user
        newUser.setPassword("password");
        newUser.setEmail("newuser@example.com");
        newUser.setName("New User");
        newUser.setRole(UserRole.USER);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(newUser);
        });
    }

    @Test
    void whenGetUserById_withExistingId_thenReturnUser() {
        // Act
        Optional<User> foundUser = userService.getUserById(user1.getId());

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("user1");
        assertThat(foundUser.get().getEmail()).isEqualTo("user1@example.com");
    }

    @Test
    void whenGetUserById_withNonExistingId_thenReturnEmpty() {
        // Act
        Optional<User> foundUser = userService.getUserById(999L);

        // Assert
        assertThat(foundUser).isEmpty();
    }

    @Test
    void whenGetUserByUsername_withExistingUsername_thenReturnUser() {
        // Act
        Optional<User> foundUser = userService.getUserByUsername("user1");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("user1");
        assertThat(foundUser.get().getEmail()).isEqualTo("user1@example.com");
    }

    @Test
    void whenGetUserByUsername_withNonExistingUsername_thenReturnEmpty() {
        // Act
        Optional<User> foundUser = userService.getUserByUsername("nonexistent");

        // Assert
        assertThat(foundUser).isEmpty();
    }

    @Test
    void whenGetAllUsers_thenReturnAllUsers() {
        // Act
        List<User> users = userService.getAllUsers();

        // Assert
        assertThat(users).hasSize(2);
        assertThat(users).extracting(User::getUsername)
                .containsExactlyInAnyOrder("user1", "user2");
    }

    @Test
    void whenUpdateUser_withValidData_thenUserIsUpdated() {
        // Arrange
        user1.setName("Updated Name");
        user1.setEmail("updated@example.com");

        // Act
        User updatedUser = userService.updateUser(user1);

        // Assert
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    void whenDeleteUser_withExistingId_thenUserIsDeleted() {
        // Act
        userService.deleteUser(user1.getId());

        // Assert
        assertThat(userRepository.findById(user1.getId())).isEmpty();
        assertThat(userRepository.count()).isEqualTo(1);
    }
} 