package com.sope.sope_ecommerce_backend.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.dto.AttributeDto;
import com.sope.sope_ecommerce_backend.dto.ImageDto;
import lombok.Data;

@Data
public class ProductVariantCreateDTO {
      private BigDecimal price;
      private int stock;
      private boolean hidden;
      private UUID productId;
      private List<AttributeDto> attributes;
      private List<ImageDto> images;
}
