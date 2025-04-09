package tqs_hw1.users.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleTest {

    @Test
    void testValidRoleValues() {
        assertTrue(UserRole.isValid("USER"));
        assertTrue(UserRole.isValid("STAFF"));
        assertTrue(UserRole.isValid("ADMIN"));
        
        // Case insensitivity check
        assertTrue(UserRole.isValid("user"));
        assertTrue(UserRole.isValid("Staff"));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"INVALID", "CUSTOMER", "RESTAURANT_STAFF", "MANAGER", "GUEST"})
    void testInvalidRoleValues(String invalidRole) {
        assertFalse(UserRole.isValid(invalidRole));
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    void testNullOrEmptyRole(String nullOrEmpty) {
        assertFalse(UserRole.isValid(nullOrEmpty));
    }
    
    @Test
    void testFromStringWithValidRole() {
        assertEquals(UserRole.USER, UserRole.fromString("USER"));
        assertEquals(UserRole.STAFF, UserRole.fromString("staff"));
        assertEquals(UserRole.ADMIN, UserRole.fromString("Admin"));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"INVALID", "CUSTOMER", "RESTAURANT_STAFF", "MANAGER", "GUEST"})
    void testFromStringWithInvalidRole(String invalidRole) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            UserRole.fromString(invalidRole);
        });
        
        assertTrue(exception.getMessage().contains("Invalid user role"));
    }
    
    @Test
    void testFromStringWithNullRole() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            UserRole.fromString(null);
        });
        
        assertEquals("Role cannot be null", exception.getMessage());
    }
} 