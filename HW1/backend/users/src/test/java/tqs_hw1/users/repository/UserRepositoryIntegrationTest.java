package tqs_hw1.users.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import tqs_hw1.users.model.User;
import tqs_hw1.users.model.UserRole;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        entityManager.clear();

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

        // Persist users to the database
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();
    }

    @Test
    void whenFindByUsername_withExistingUsername_thenReturnUser() {
        // Act
        Optional<User> found = userRepository.findByUsername("user1");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("user1");
        assertThat(found.get().getEmail()).isEqualTo("user1@example.com");
        assertThat(found.get().getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void whenFindByUsername_withNonExistingUsername_thenReturnEmpty() {
        // Act
        Optional<User> found = userRepository.findByUsername("nonexistent");

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void whenFindByEmail_withExistingEmail_thenReturnUser() {
        // Act
        Optional<User> found = userRepository.findByEmail("user1@example.com");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("user1");
        assertThat(found.get().getEmail()).isEqualTo("user1@example.com");
    }

    @Test
    void whenFindByEmail_withNonExistingEmail_thenReturnEmpty() {
        // Act
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void whenExistsByUsername_withExistingUsername_thenReturnTrue() {
        // Act
        boolean exists = userRepository.existsByUsername("user1");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void whenExistsByUsername_withNonExistingUsername_thenReturnFalse() {
        // Act
        boolean exists = userRepository.existsByUsername("nonexistent");

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    void whenExistsByEmail_withExistingEmail_thenReturnTrue() {
        // Act
        boolean exists = userRepository.existsByEmail("user1@example.com");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    void whenExistsByEmail_withNonExistingEmail_thenReturnFalse() {
        // Act
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Assert
        assertThat(exists).isFalse();
    }
} 