package tqs_hw1.users.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setFullName("Test User");
        user.setPhone("123-456-7890");
        user.setRole(UserRole.CUSTOMER);
        
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        user.setLastLogin(now);
        
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("Test User", user.getFullName());
        assertEquals("123-456-7890", user.getPhone());
        assertEquals(UserRole.CUSTOMER, user.getRole());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getLastLogin());
    }
    
    @Test
    void testOnCreate() {
        User user = new User();
        assertNull(user.getCreatedAt());
        
        user.onCreate();
        
        assertNotNull(user.getCreatedAt());
        assertTrue(user.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(user.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
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