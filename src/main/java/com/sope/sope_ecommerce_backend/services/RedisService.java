package com.sope.sope_ecommerce_backend.services;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // save data with TTL
    public void set(String key, Object value, long ttl, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, ttl, unit);
    }

    // get data
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // remove data
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}

