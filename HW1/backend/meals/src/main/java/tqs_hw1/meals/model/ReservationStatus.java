package tqs_hw1.meals.model;

/**
 * Enum representing the possible states of a reservation.
 */
public enum ReservationStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED;
    
    /**
     * Checks if a string is a valid reservation status.
     *
     * @param status The status string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String status) {
        if (status == null) {
            return false;
        }
        
        try {
            ReservationStatus.valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Converts a string to a ReservationStatus enum if valid.
     *
     * @param status The status string to convert
     * @return The corresponding ReservationStatus enum value
     * @throws IllegalArgumentException if the status is invalid
     */
    public static ReservationStatus fromString(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        
        try {
            return ReservationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid reservation status: " + status +
                    ". Valid values are: PENDING, CONFIRMED, CANCELLED, COMPLETED");
        }
    }
} 