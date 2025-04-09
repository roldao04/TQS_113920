package tqs_hw1.users.dto;

import org.junit.jupiter.api.Test;
import tqs_hw1.users.model.UserRole;

import static org.assertj.core.api.Assertions.assertThat;

class AuthResponseTest {

    @Test
    void testAuthResponseCreation() {
        // Arrange & Act
        AuthResponse response = new AuthResponse(true, UserRole.USER, "Test message");

        // Assert
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getRole()).isEqualTo(UserRole.USER);
        assertThat(response.getMessage()).isEqualTo("Test message");
    }

    @Test
    void testAuthResponseSettersAndGetters() {
        // Arrange
        AuthResponse response = new AuthResponse(false, null, "Initial message");

        // Act
        response.setSuccess(true);
        response.setRole(UserRole.ADMIN);
        response.setMessage("Updated message");

        // Assert
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getRole()).isEqualTo(UserRole.ADMIN);
        assertThat(response.getMessage()).isEqualTo("Updated message");
    }

    @Test
    void testAuthResponseEquality() {
        // Arrange
        AuthResponse response1 = new AuthResponse(true, UserRole.USER, "Test message");
        AuthResponse response2 = new AuthResponse(true, UserRole.USER, "Test message");
        AuthResponse response3 = new AuthResponse(false, UserRole.ADMIN, "Different message");

        // Assert
        assertThat(response1).isEqualTo(response2);
        assertThat(response1).isNotEqualTo(response3);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        assertThat(response1.hashCode()).isNotEqualTo(response3.hashCode());
    }
} 