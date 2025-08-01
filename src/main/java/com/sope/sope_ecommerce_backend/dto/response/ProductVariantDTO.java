package com.sope.sope_ecommerce_backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import lombok.Data;

@Data
public class ProductVariantDTO {
      private UUID productVariantId;
      private BigDecimal price;
      private int stock;
      private boolean hidden;
      private LocalDateTime createdAt;
      private UUID productId;
      private Set<AttributeDTO> attributes;
      private String imageVariant;

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

      public Set<AttributeDTO> getAttributes() {
            return attributes;
      }

      public void setAttributes(Set<AttributeDTO> attributes) {
            this.attributes = attributes;
      }

      public String getImageVariant() {
            return imageVariant;
      }

      public void setImageVariant(String imageVariant) {
            this.imageVariant = imageVariant;
      }
}