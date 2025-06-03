package com.example.shopapi.service;

import com.example.shopapi.model.UserDto;

public interface AuthService {
    String authenticate(String username, String password);
} 