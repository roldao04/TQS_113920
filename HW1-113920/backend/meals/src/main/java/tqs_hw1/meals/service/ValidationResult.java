package tqs_hw1.meals.service;

/**
 * Class to hold validation results
 */
public class ValidationResult {
    private final boolean valid;
    private final String errorMessage;
    
    public ValidationResult(boolean valid, String errorMessage) {
        this.valid = valid;
        this.errorMessage = errorMessage;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public static ValidationResult valid() {
        return new ValidationResult(true, null);
    }
    
    public static ValidationResult invalid(String errorMessage) {
        return new ValidationResult(false, errorMessage);
    }
} 