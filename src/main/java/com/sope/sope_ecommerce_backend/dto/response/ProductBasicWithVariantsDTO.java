package com.sope.sope_ecommerce_backend.dto.response;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBasicWithVariantsDTO {
      private UUID productId;
      private String name;
      private List<Map<String, String>> variants;
}