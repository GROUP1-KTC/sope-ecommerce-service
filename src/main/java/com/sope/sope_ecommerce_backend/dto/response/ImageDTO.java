package com.sope.sope_ecommerce_backend.dto.response;

import lombok.Data;

@Data
public class ImageDTO {
      private Long imageId;
      private String url;
      private int priority;

      public Long getImageId() {
            return imageId;
      }
      public void setImageId(Long imageId) {
            this.imageId = imageId;
      }
      public String getUrl() {
            return url;
      }

      public void setUrl(String url) {
            this.url = url;
      }

      public int getPriority() {
            return priority;
      }

      public void setPriority(int priority) {
            this.priority = priority;
      }
}
