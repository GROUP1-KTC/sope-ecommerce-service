package com.sope.sope_ecommerce_backend.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.dto.response.AttributeDTO;
import com.sope.sope_ecommerce_backend.entities.Dimension;

import lombok.Data;

@Data
public class ProductVariantRequestDTO {
      private UUID productVariantId;
      private BigDecimal price;
      private Integer stock;
      private List<AttributeDTO> attributes;
      private String imageVariant;
      private Dimension dimension;
      private BigDecimal weight;

}