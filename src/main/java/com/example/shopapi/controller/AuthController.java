package com.example.shopapi.controller;

import com.example.shopapi.model.AuthRequest;
import com.example.shopapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
        summary = "Authenticate user",
        description = "Authenticates a user with username and password and returns a base64 encoded token"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Authentication successful",
            content = @Content(schema = @Schema(implementation = String.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Authentication failed"
        )
    })
    public ResponseEntity<String> authenticate(@RequestBody AuthRequest request) {
        log.info("Received authentication request for user: {}", request.getUsername());
        String token = authService.authenticate(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(token);
    }
} 