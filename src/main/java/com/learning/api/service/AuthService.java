package com.learning.api.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.learning.api.dto.AuthResponseDTO;
import com.learning.api.dto.LoginRequestDTO;
import com.learning.api.dto.SignupRequestDTO;
import com.learning.api.entity.User;
import com.learning.api.repository.UserRepository;
import com.learning.api.util.JwtUtil;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil,
                       TokenBlacklistService tokenBlacklistService) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    public AuthResponseDTO signup(SignupRequestDTO requestDTO) {
        // Validate password before anything else
        String passwordError = validatePassword(requestDTO.getPassword());
        if (passwordError != null) {
            return new AuthResponseDTO(passwordError, null);
        }

        // Check if email already exists
        Optional<User> existingUser = userRepository.findByEmail(requestDTO.getEmail());
        if (existingUser.isPresent()) {
            return new AuthResponseDTO("Email already exists", null);
        }

        // Create new user from DTO
        User user = new User(
                requestDTO.getName(),
                requestDTO.getEmail(),
                requestDTO.getPassword()
        );

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getEmail());
        return new AuthResponseDTO("Signup successful", savedUser, token);
    }

    public AuthResponseDTO login(LoginRequestDTO requestDTO) {
        // Find user by email
        Optional<User> user = userRepository.findByEmail(requestDTO.getEmail());
        if (user.isEmpty()) {
            return new AuthResponseDTO("User not found with email: " + requestDTO.getEmail(), null);
        }

        // Check password match
        if (!user.get().getPassword().equals(requestDTO.getPassword())) {
            return new AuthResponseDTO("Invalid password", null);
        }

       
        return new AuthResponseDTO("Login successful", user.get());
    }

    // Logout — blacklist the current token in Redis
    public AuthResponseDTO logout(String token) {
        tokenBlacklistService.blacklistToken(token);
        return new AuthResponseDTO("Logout successful", null);
    }

    private String validatePassword(String password) {
        if (password == null || password.length() < 5) {
            return "Password must be at least 5 characters long";
        }

        boolean hasUpperCase = false;
        boolean hasSpecialChar = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUpperCase = true;
            }
            if (!Character.isLetterOrDigit(ch)) {
                hasSpecialChar = true;
            }
        }

        if (!hasUpperCase) {
            return "Password must contain at least one capital letter";
        }
        if (!hasSpecialChar) {
            return "Password must contain at least one special character";
        }

        return null;
    }
}
