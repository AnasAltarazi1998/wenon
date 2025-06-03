package com.example.shopapi.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication request containing username and password")
public class AuthRequest {
    @Schema(description = "Username for authentication", example = "admin")
    private String username;
    
    @Schema(description = "Password for authentication", example = "admin")
    private String password;
} 