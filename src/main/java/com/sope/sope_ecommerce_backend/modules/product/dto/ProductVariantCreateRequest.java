package com.sope.sope_ecommerce_backend.modules.product.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ProductVariantCreateRequest {
      private BigDecimal price;
      private int stock;
      private boolean hidden;
      private UUID productId;
      private List<AttributeDto> attributes;
      private List<ImageDto> images;
}
