package com.example.shopapi.service.impl;

import com.example.shopapi.service.AuthService;
import com.example.shopapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Override
    public String authenticate(String username, String password) {
        log.info("Authenticating user: {}", username);
        
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
        
        // Set authentication in context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Create token string (username:password)
        String tokenString = username + ":" + password;
        
        // Encode to Base64
        return Base64.getEncoder().encodeToString(tokenString.getBytes());
    }
} 