package tqs_hw1.users.dto;

import org.junit.jupiter.api.Test;
import tqs_hw1.users.model.UserRole;

import static org.assertj.core.api.Assertions.assertThat;

class RegisterRequestTest {

    @Test
    void testRegisterRequestSettersAndGetters() {
        // Arrange
        RegisterRequest request = new RegisterRequest();

        // Act
        request.setUsername("testuser");
        request.setPassword("testpassword");
        request.setEmail("test@example.com");
        request.setName("Test User");
        request.setRole(UserRole.USER);
        request.setAdminPassword("adminpass");

        // Assert
        assertThat(request.getUsername()).isEqualTo("testuser");
        assertThat(request.getPassword()).isEqualTo("testpassword");
        assertThat(request.getEmail()).isEqualTo("test@example.com");
        assertThat(request.getName()).isEqualTo("Test User");
        assertThat(request.getRole()).isEqualTo(UserRole.USER);
        assertThat(request.getAdminPassword()).isEqualTo("adminpass");
    }

    @Test
    void testRegisterRequestEquality() {
        // Arrange
        RegisterRequest request1 = new RegisterRequest();
        request1.setUsername("testuser");
        request1.setPassword("testpassword");
        request1.setEmail("test@example.com");
        request1.setName("Test User");
        request1.setRole(UserRole.USER);
        request1.setAdminPassword("adminpass");

        RegisterRequest request2 = new RegisterRequest();
        request2.setUsername("testuser");
        request2.setPassword("testpassword");
        request2.setEmail("test@example.com");
        request2.setName("Test User");
        request2.setRole(UserRole.USER);
        request2.setAdminPassword("adminpass");

        RegisterRequest request3 = new RegisterRequest();
        request3.setUsername("differentuser");
        request3.setPassword("differentpassword");
        request3.setEmail("different@example.com");
        request3.setName("Different User");
        request3.setRole(UserRole.ADMIN);
        request3.setAdminPassword("differentpass");

        // Assert
        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1.hashCode()).isNotEqualTo(request3.hashCode());
    }
} 