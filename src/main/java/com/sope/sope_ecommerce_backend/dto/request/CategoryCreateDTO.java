package com.sope.sope_ecommerce_backend.dto.request;

import lombok.Data;
import java.util.UUID;

@Data
public class CategoryCreateDTO {
      private String name;
      private UUID parentId; // optional
}
