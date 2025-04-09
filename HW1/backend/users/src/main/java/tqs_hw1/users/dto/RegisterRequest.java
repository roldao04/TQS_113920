package tqs_hw1.users.dto;

import lombok.Data;
import tqs_hw1.users.model.UserRole;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String name;
    private UserRole role;
    private String adminPassword;
} 