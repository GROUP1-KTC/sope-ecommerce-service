package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.OrderItemRequest;
import com.sope.sope_ecommerce_backend.entities.ProductVariant;
import com.sope.sope_ecommerce_backend.repositories.ProductVariantRepository;
import com.sope.sope_ecommerce_backend.services.ProductVariantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;

    @Override
    public ProductVariant getProductVariantEntityById(UUID productVariantId) {
        return productVariantRepository.findById(productVariantId)
                .orElseThrow(() -> new IllegalArgumentException("Product variant not found with ID: " + productVariantId));
    }


    @Transactional
    @CacheEvict(value = "products", key = "'allProducts'")
    @Override
    public void updateStockBatch(Map<UUID, Integer> stockChanges) {
        // Lấy tất cả variant theo id
        List<ProductVariant> variants = productVariantRepository.findAllById(stockChanges.keySet());

        if (variants.size() != stockChanges.size()) {
            throw new IllegalArgumentException("Một hoặc nhiều sản phẩm không tồn tại");
        }

        // Giảm tồn kho
        for (ProductVariant variant : variants) {
            int qtyToDecrease = stockChanges.get(variant.getProductVariantId());
            if (variant.getStock() < qtyToDecrease) {
                throw new IllegalArgumentException("Sản phẩm " + variant.getProductVariantId() + " không đủ tồn kho");
            }

            log.info("Giảm tồn kho sản phẩm {}: -{}", variant.getProductVariantId(), qtyToDecrease);
            variant.setStock(variant.getStock() - qtyToDecrease);
            variant.setSold(variant.getSold() + qtyToDecrease);
        }

        // Batch save
        log.info("Cập nhật tồn kho cho {} sản phẩm", variants.size());
        productVariantRepository.saveAll(variants);
    }


    @Transactional
    public void updateStockBatchFromOrderItems(List<OrderItemRequest> items) {
        Map<UUID, Integer> stockChanges = items.stream()
                .collect(Collectors.toMap(
                        OrderItemRequest::productVariantId,
                        OrderItemRequest::quantity,
                        Integer::sum
                ));
        updateStockBatch(stockChanges);
    }



    @Override
    @Transactional
    public void saveProductVariant(ProductVariant productVariant) {
        productVariantRepository.save(productVariant);
    }

    @Override
    @Transactional
    @CacheEvict(value = "products", key = "'allProducts'")
    public void retrieveProductVariantStock(UUID productVariantId, int quantityChange) {
        ProductVariant variant = getProductVariantEntityById(productVariantId);
        int newStock = variant.getStock() + quantityChange;
        int soldQuantity = variant.getSold() - quantityChange;

        if (newStock < 0) {
            throw new IllegalArgumentException("Insufficient stock for product variant ID: " + productVariantId);
        }
        variant.setStock(newStock);
        variant.setSold(soldQuantity);
        productVariantRepository.save(variant);
    }
}
