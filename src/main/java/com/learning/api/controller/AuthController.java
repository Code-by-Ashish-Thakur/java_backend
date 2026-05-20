package com.learning.api.controller;

import com.learning.api.dto.AuthResponseDTO;
import com.learning.api.dto.LoginRequestDTO;
import com.learning.api.dto.SignupRequestDTO;
import com.learning.api.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public AuthResponseDTO signup(@RequestBody SignupRequestDTO requestDTO) {
        return authService.signup(requestDTO);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginRequestDTO requestDTO) {
        return authService.login(requestDTO);
    }
}
