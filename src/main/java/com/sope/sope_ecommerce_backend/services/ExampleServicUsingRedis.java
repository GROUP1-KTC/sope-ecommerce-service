package com.sope.sope_ecommerce_backend.services;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ExampleServicUsingRedis {
    @Cacheable(cacheNames = "users", key = "#id")
    public String getUserById(String id) {
        simulateSlow();
        return "User_" + id;
    }

    // Use @CachePut for updates that return the new value, to update cache directly
    @CachePut(cacheNames = "users", key = "#id")
    public String updateUserById(String id, String newData) {
        // Simulate update logic
        simulateSlow();
        return newData; // Return updated value to store in cache
    }

    // Evict if no return value or for deletions
    @CacheEvict(cacheNames = "users", key = "#id")
    public void deleteUserById(String id) {
        // Delete logic
    }

    private void simulateSlow() {
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
    }
}
