package com.learning.api.service;

import com.learning.api.dto.AuthResponseDTO;
import com.learning.api.dto.LoginRequestDTO;
import com.learning.api.dto.SignupRequestDTO;
import com.learning.api.entity.User;
import com.learning.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuthResponseDTO signup(SignupRequestDTO requestDTO) {
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
        return new AuthResponseDTO("Signup successful", savedUser);
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
}
