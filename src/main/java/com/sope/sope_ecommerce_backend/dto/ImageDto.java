package com.sope.sope_ecommerce_backend.dto;

import lombok.Data;

@Data
public class ImageDto {
      private String url;
      private int priority;

      // Getters and setters
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
