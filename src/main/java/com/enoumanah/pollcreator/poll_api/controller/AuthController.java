package com.enoumanah.pollcreator.poll_api.controller;

import com.enoumanah.pollcreator.poll_api.dto.LoginRequest;
import com.enoumanah.pollcreator.poll_api.dto.RegisterRequest;
import com.enoumanah.pollcreator.poll_api.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }
}

// Helper classes can remain in the same file or be moved to the dto package
class AuthResponse {
    private String token;
    public AuthResponse(String token) { this.token = token; }
    public String getToken() { return token; }
}