package com.enoumanah.pollcreator.poll_api.service;

import com.enoumanah.pollcreator.poll_api.dto.RegisterRequest;
import com.enoumanah.pollcreator.poll_api.model.User;
import com.enoumanah.pollcreator.poll_api.repository.UserRepository;
import com.enoumanah.pollcreator.poll_api.security.JwtTokenProvider;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder encoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        userRepository.save(user);
        return jwtTokenProvider.generateTokenFromUsername(user.getUsername());
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !encoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return jwtTokenProvider.generateTokenFromUsername(username);
    }
}
