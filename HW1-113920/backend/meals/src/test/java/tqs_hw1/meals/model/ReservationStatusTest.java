package tqs_hw1.meals.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ReservationStatusTest {

    @Test
    void testValidStatusValues() {
        assertTrue(ReservationStatus.isValid("PENDING"));
        assertTrue(ReservationStatus.isValid("CONFIRMED"));
        assertTrue(ReservationStatus.isValid("CANCELLED"));
        assertTrue(ReservationStatus.isValid("COMPLETED"));
        
        // Case insensitivity check
        assertTrue(ReservationStatus.isValid("pending"));
        assertTrue(ReservationStatus.isValid("Confirmed"));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"INVALID", "DONE", "IN_PROGRESS", "ACTIVE"})
    void testInvalidStatusValues(String invalidStatus) {
        assertFalse(ReservationStatus.isValid(invalidStatus));
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    void testNullOrEmptyStatus(String nullOrEmpty) {
        assertFalse(ReservationStatus.isValid(nullOrEmpty));
    }
    
    @Test
    void testFromStringWithValidStatus() {
        assertEquals(ReservationStatus.PENDING, ReservationStatus.fromString("PENDING"));
        assertEquals(ReservationStatus.CONFIRMED, ReservationStatus.fromString("confirmed"));
        assertEquals(ReservationStatus.CANCELLED, ReservationStatus.fromString("Cancelled"));
        assertEquals(ReservationStatus.COMPLETED, ReservationStatus.fromString("cOMpLeTed"));
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"INVALID", "DONE", "IN_PROGRESS", "ACTIVE"})
    void testFromStringWithInvalidStatus(String invalidStatus) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ReservationStatus.fromString(invalidStatus);
        });
        
        assertTrue(exception.getMessage().contains("Invalid reservation status"));
    }
    
    @Test
    void testFromStringWithNullStatus() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ReservationStatus.fromString(null);
        });
        
        assertEquals("Status cannot be null", exception.getMessage());
    }
} 