package com.sope.sope_ecommerce_backend.modules.product.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CategoryCreateRequest {
      private String name;
      private UUID parentId; // optional
}
