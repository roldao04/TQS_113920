package tqs_hw1.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import tqs_hw1.users.model.UserRole;

@Data
@AllArgsConstructor
public class AuthResponse {
    private boolean success;
    private UserRole role;
    private String message;
} 