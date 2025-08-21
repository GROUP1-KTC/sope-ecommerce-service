package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.entities.ProductVariant;
import com.sope.sope_ecommerce_backend.repositories.ProductVariantRepository;
import com.sope.sope_ecommerce_backend.services.ProductVariantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;

    @Override
    public ProductVariant getProductVariantEntityById(UUID productVariantId) {
        return productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new IllegalArgumentException("Product variant not found with ID: " + productVariantId));
    }

    @Override
    public void updateProductVariantStock(UUID productVariantId, int quantityChange) {
        ProductVariant variant = getProductVariantEntityById(productVariantId);
        int newStock = variant.getStock() + quantityChange;
        if (newStock < 0) {
            throw new IllegalArgumentException("Insufficient stock for product variant ID: " + productVariantId);
        }
        variant.setStock(newStock);
        productVariantRepository.save(variant);
    }
}
