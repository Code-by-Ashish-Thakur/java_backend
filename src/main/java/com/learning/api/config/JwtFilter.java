package com.learning.api.config;

import com.learning.api.service.TokenBlacklistService;
import com.learning.api.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtFilter(JwtUtil jwtUtil, TokenBlacklistService tokenBlacklistService) {
        this.jwtUtil = jwtUtil;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Step 1: Get the Authorization header
        String authHeader = request.getHeader("Authorization");

        // Step 2: Check if header exists and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // Step 3: Extract the token (remove "Bearer " prefix)
            String token = authHeader.substring(7);

            try {
                // Step 4: Check if this token has been blacklisted (user logged out)
                if (tokenBlacklistService.isTokenBlacklisted(token)) {
                    // Token is blacklisted — skip authentication, Spring Security will reject
                    filterChain.doFilter(request, response);
                    return;
                }

                // Step 5: Extract email from token
                String email = jwtUtil.extractEmail(token);

                // Step 6: If email is valid and no authentication exists yet
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // Step 7: Validate the token
                    if (jwtUtil.isTokenValid(token, email)) {

                        // Step 8: Tell Spring Security "this user is authenticated"
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                // Invalid token — do nothing, Spring Security will block the request
            }
        }

        // Step 9: Continue to the next filter / controller
        filterChain.doFilter(request, response);
    }
}
