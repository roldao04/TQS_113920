package tqs_hw1.users.model;

/**
 * Enum representing the possible roles of a user.
 */
public enum UserRole {
    USER,
    STAFF,
    ADMIN;
    
    /**
     * Checks if a string is a valid user role.
     *
     * @param role The role string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValid(String role) {
        if (role == null) {
            return false;
        }
        
        try {
            UserRole.valueOf(role.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * Converts a string to a UserRole enum if valid.
     *
     * @param role The role string to convert
     * @return The corresponding UserRole enum value
     * @throws IllegalArgumentException if the role is invalid
     */
    public static UserRole fromString(String role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid user role: " + role +
                    ". Valid values are: USER, STAFF, ADMIN");
        }
    }
} 