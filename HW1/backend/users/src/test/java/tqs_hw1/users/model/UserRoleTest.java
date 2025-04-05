package tqs_hw1.users.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleTest {

    @Test
    void testValidRoleValues() {
        assertTrue(UserRole.isValid("CUSTOMER"));
        assertTrue(UserRole.isValid("RESTAURANT_STAFF"));
        assertTrue(UserRole.isValid("ADMIN"));
        
        // Case insensitivity check
        assertTrue(UserRole.isValid("customer"));
        assertTrue(UserRole.isValid("Restaurant_Staff"));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"INVALID", "USER", "STAFF", "MANAGER", "GUEST"})
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
        assertEquals(UserRole.CUSTOMER, UserRole.fromString("CUSTOMER"));
        assertEquals(UserRole.RESTAURANT_STAFF, UserRole.fromString("restaurant_staff"));
        assertEquals(UserRole.ADMIN, UserRole.fromString("Admin"));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"INVALID", "USER", "STAFF", "MANAGER", "GUEST"})
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