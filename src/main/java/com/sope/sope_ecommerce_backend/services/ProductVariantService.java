package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.entities.ProductVariant;

import java.util.UUID;

public interface ProductVariantService {
    ProductVariant getProductVariantEntityById(UUID productVariantId);

    void updateProductVariantStock(UUID productVariantId, int quantityChange);
}
