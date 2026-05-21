package com.learning.api.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.learning.api.util.JwtUtil;

// ============================================================
// TOKEN BLACKLIST SERVICE
// ============================================================
// When a user logs out, their JWT token is added to a Redis blacklist.
// The JwtFilter checks this blacklist BEFORE allowing any request.
//
// WHY RedisTemplate instead of @Cacheable?
//   Because each blacklisted token needs its own TTL (= time until
//   the token would have expired naturally). @Cacheable only supports
//   a single TTL per cache name, not per entry.
//
// Redis key:   jwt:blacklist:<full-token-string>
// Redis value: "blacklisted"
// Redis TTL:   remaining lifetime of the token (auto-cleanup)
// ============================================================

@Service
public class TokenBlacklistService {

    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate, JwtUtil jwtUtil) {
        this.redisTemplate = redisTemplate;
        this.jwtUtil = jwtUtil;
    }

    // Blacklist a token — stores it in Redis with TTL = remaining token lifetime
    public void blacklistToken(String token) {
        long expirationTime = jwtUtil.getExpiration(token).getTime();
        long now = System.currentTimeMillis();
        long ttlMillis = expirationTime - now;

        // Only blacklist if the token hasn't already expired
        if (ttlMillis > 0) {
            String key = BLACKLIST_PREFIX + token;
            redisTemplate.opsForValue().set(key, "blacklisted", ttlMillis, TimeUnit.MILLISECONDS);
        }
    }

    // Check if a token has been blacklisted
    public boolean isTokenBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
