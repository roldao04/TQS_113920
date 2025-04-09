package tqs_hw1.users.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
} 