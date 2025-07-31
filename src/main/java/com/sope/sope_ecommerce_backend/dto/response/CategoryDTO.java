package com.sope.sope_ecommerce_backend.dto.response;

import java.util.UUID;
import lombok.Data;

@Data
public class CategoryDTO {
      private UUID id;
      private String name;
      private String slug;
      private ParentInfo parent;

      @Data
      public static class ParentInfo {
            private UUID id;
            private String name;
      }
}
