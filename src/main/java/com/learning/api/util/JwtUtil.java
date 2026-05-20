package com.learning.api.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Fixed secret key — stays the same even after server restart
    // (in production, store this in application.properties or environment variable)
    private final Key secretKey = Keys.hmacShaKeyFor(
            "my-super-secret-key-that-is-at-least-32-characters-long".getBytes()
    );

    // Token valid for 24 hours (in milliseconds)
    private final long expirationTime = 24 * 60 * 60 * 1000;

    // Generate a JWT token for the given email
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)                                          // who this token belongs to
                .setIssuedAt(new Date())                                    // when token was created
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))  // when it expires
                .signWith(secretKey)                                        // sign with our secret key
                .compact();
    }

    // Extract the email from a token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // Check if token is valid (not expired and email matches)
    public boolean isTokenValid(String token, String email) {
        String tokenEmail = extractEmail(token);
        return tokenEmail.equals(email) && !isTokenExpired(token);
    }

    // Check if token has expired
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Extract all claims (data) from the token
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
