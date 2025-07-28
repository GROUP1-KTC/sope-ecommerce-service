package com.sope.sope_ecommerce_backend.modules.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class ProductVariantResponse {
      private UUID productVariantId;
      private BigDecimal price;
      private int stock;
      private boolean hidden;
      private String slug;
      private LocalDateTime createdAt;
      private UUID productId;
      private Set<AttributeDto> attributes;
      private List<ImageDto> images;

      // Getters and setters
      public UUID getProductVariantId() {
            return productVariantId;
      }

      public void setProductVariantId(UUID productVariantId) {
            this.productVariantId = productVariantId;
      }

      public BigDecimal getPrice() {
            return price;
      }

      public void setPrice(BigDecimal price) {
            this.price = price;
      }

      public int getStock() {
            return stock;
      }

      public void setStock(int stock) {
            this.stock = stock;
      }

      public boolean isHidden() {
            return hidden;
      }

      public void setHidden(boolean hidden) {
            this.hidden = hidden;
      }

      public String getSlug() {
            return slug;
      }

      public void setSlug(String slug) {
            this.slug = slug;
      }

      public LocalDateTime getCreatedAt() {
            return createdAt;
      }

      public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
      }

      public UUID getProductId() {
            return productId;
      }

      public void setProductId(UUID productId) {
            this.productId = productId;
      }

      public Set<AttributeDto> getAttributes() {
            return attributes;
      }

      public void setAttributes(Set<AttributeDto> attributes) {
            this.attributes = attributes;
      }

      public List<ImageDto> getImages() {
            return images;
      }

      public void setImages(List<ImageDto> images) {
            this.images = images;
      }
}