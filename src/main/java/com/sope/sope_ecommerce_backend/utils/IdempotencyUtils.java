package com.sope.sope_ecommerce_backend.utils;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.function.Supplier;

public class IdempotencyUtils {
    private IdempotencyUtils() {} // prevent instantiation

    public static <T> T saveWithIdempotency(
            Supplier<T> saveOperation,
            Supplier<Optional<T>> findByKey,
            RuntimeException ex) {

        try {
            return saveOperation.get();
        } catch (DataIntegrityViolationException e) {
            return findByKey.get().orElseThrow(() -> ex);
        }
    }
}
