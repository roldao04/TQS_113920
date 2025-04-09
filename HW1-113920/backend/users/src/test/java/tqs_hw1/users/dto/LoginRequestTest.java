package tqs_hw1.users.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    @Test
    void testLoginRequestSettersAndGetters() {
        // Arrange
        LoginRequest request = new LoginRequest();

        // Act
        request.setUsername("testuser");
        request.setPassword("testpassword");

        // Assert
        assertThat(request.getUsername()).isEqualTo("testuser");
        assertThat(request.getPassword()).isEqualTo("testpassword");
    }

    @Test
    void testLoginRequestEquality() {
        // Arrange
        LoginRequest request1 = new LoginRequest();
        request1.setUsername("testuser");
        request1.setPassword("testpassword");

        LoginRequest request2 = new LoginRequest();
        request2.setUsername("testuser");
        request2.setPassword("testpassword");

        LoginRequest request3 = new LoginRequest();
        request3.setUsername("differentuser");
        request3.setPassword("differentpassword");

        // Assert
        assertThat(request1).isEqualTo(request2);
        assertThat(request1).isNotEqualTo(request3);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1.hashCode()).isNotEqualTo(request3.hashCode());
    }
} 