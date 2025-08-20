package com.sope.sope_ecommerce_backend.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
      private Long reviewId;
      private Integer rating;
      private String content;
      private UserInfo user;
      private ProductVariantDTO productVariant;
      private LocalDateTime createdAt;
      private LocalDateTime updatedAt;
      private List<ImageDTO> imagesList;

      @Data
      public static class UserInfo {
            private UUID id;
            private String username;
      }

}
