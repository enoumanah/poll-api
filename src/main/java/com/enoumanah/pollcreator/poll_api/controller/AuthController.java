package com.enoumanah.pollcreator.poll_api.controller;

import com.enoumanah.pollcreator.poll_api.dto.LoginRequest;
import com.enoumanah.pollcreator.poll_api.dto.RegisterRequest;
import com.enoumanah.pollcreator.poll_api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;



    @PostMapping(value = "/register", consumes = "application/json")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            String token = authService.register(request);
            return ResponseEntity.status(201).body(new AuthResponse(token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(new ErrorResponse(e.getMessage()));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Database unavailable")) {
                return ResponseEntity.status(503).body(new ErrorResponse("Database unavailable"));
            }
            return ResponseEntity.status(500).body(new ErrorResponse("Internal server error"));
        }
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(new ErrorResponse(e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Internal server error"));
        }
    }
}

class AuthResponse {
    private String token;
    public AuthResponse(String token) { this.token = token; }
    public String getToken() { return token; }
}

class ErrorResponse {
    private String message;
    public ErrorResponse(String message) { this.message = message; }
    public String getMessage() { return message; }
}