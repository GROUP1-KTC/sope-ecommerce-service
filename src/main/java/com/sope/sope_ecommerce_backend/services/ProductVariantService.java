package com.sope.sope_ecommerce_backend.services;

import com.sope.sope_ecommerce_backend.entities.ProductVariant;

import java.util.Map;
import java.util.UUID;

public interface ProductVariantService {
    ProductVariant getProductVariantEntityById(UUID productVariantId);

    void saveProductVariant(ProductVariant productVariant);

    void retrieveProductVariantStock(UUID productVariantId, int quantityChange);

    void updateStockBatch(Map<UUID, Integer> stockChanges);
}
