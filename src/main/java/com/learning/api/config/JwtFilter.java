package com.learning.api.config;

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

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
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
                // Step 4: Extract email from token
                String email = jwtUtil.extractEmail(token);

                // Step 5: If email is valid and no authentication exists yet
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // Step 6: Validate the token
                    if (jwtUtil.isTokenValid(token, email)) {

                        // Step 7: Tell Spring Security "this user is authenticated"
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                // Invalid token — do nothing, Spring Security will block the request
            }
        }

        // Step 8: Continue to the next filter / controller
        filterChain.doFilter(request, response);
    }
}
