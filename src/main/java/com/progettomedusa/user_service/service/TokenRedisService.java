package com.progettomedusa.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenRedisService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // Salva il token in Redis
    public void storeToken(String key, String email) {
        redisTemplate.opsForValue().set(key, email, Duration.ofMinutes(65));
    }

    public String consumeToken(String key) {
        String email = redisTemplate.opsForValue().get(key);
        return email;
    }
}