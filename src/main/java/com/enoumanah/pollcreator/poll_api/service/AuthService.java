package com.enoumanah.pollcreator.poll_api.service;

import com.enoumanah.pollcreator.poll_api.dto.RegisterRequest;
import com.enoumanah.pollcreator.poll_api.model.User;
import com.enoumanah.pollcreator.poll_api.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final Key jwtSecretKey;

    public AuthService(UserRepository userRepository, PasswordEncoder encoder, @Value("${app.jwt.secret}") String jwtSecret) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtSecretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String register(RegisterRequest request) {
        try {
            if (userRepository.findByUsername(request.getUsername()) != null) {
                throw new IllegalArgumentException("Username already exists");
            }
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(encoder.encode(request.getPassword()));
            user.setEmail(request.getEmail());
            userRepository.save(user);
            return generateJwtToken(user.getUsername());
        } catch (DataAccessResourceFailureException e) {
            throw new RuntimeException("Database unavailable", e);
        } catch (Exception e) {
            throw new RuntimeException("Registration failed", e);
        }
    }

    public String login(String username, String password) {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null || !encoder.matches(password, user.getPassword())) {
                throw new IllegalArgumentException("Invalid username or password");
            }
            return generateJwtToken(username);
        } catch (DataAccessResourceFailureException e) {
            throw new RuntimeException("Database unavailable", e);
        } catch (Exception e) {
            throw new RuntimeException("Login failed", e);
        }
    }

    private String generateJwtToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .signWith(jwtSecretKey)
                .compact();
    }
}