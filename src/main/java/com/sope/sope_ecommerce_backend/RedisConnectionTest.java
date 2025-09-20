package com.sope.sope_ecommerce_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisConnectionTest implements CommandLineRunner {

    private final StringRedisTemplate redisTemplate;

    public RedisConnectionTest(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            redisTemplate.opsForValue().set("testKey", "Hello Redis");
            String value = redisTemplate.opsForValue().get("testKey");
            System.out.println("✅ Redis connection OK - Value: " + value);
        } catch (Exception e) {
            System.err.println("❌ Redis connection failed: " + e.getMessage());
        }
    }
}
