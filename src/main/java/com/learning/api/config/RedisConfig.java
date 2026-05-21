package com.learning.api.config;

import java.time.Duration;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

// ============================================================
// REDIS CONFIGURATION
// ============================================================
// Two concerns:
//   1. RedisCacheManager — powers @Cacheable/@CacheEvict for Student/Employee
//   2. RedisTemplate     — used directly for JWT token blacklisting
//
// @EnableCaching activates Spring's cache abstraction so that
// @Cacheable, @CacheEvict, @CachePut annotations work.
// ============================================================

@Configuration
@EnableCaching
public class RedisConfig {

    // ---- Cache Manager for @Cacheable ----
    // Uses JSON serialization so data is human-readable in redis-cli
    // Default TTL: 10 minutes (safety net — @CacheEvict handles staleness on writes)
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .build();
    }

    // ---- RedisTemplate for JWT blacklisting (manual operations) ----
    // Separate from cache manager because we need per-key TTL control
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
