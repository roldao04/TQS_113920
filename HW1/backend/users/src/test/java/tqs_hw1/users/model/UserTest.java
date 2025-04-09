package tqs_hw1.users.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setRole(UserRole.USER);
        
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getName());
        assertEquals(UserRole.USER, user.getRole());
    }
    
    @Test
    void testUserEquality() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("testuser");
        
        User user2 = new User();
        user2.setId(1L);
        user2.setUsername("testuser");
        
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
} 