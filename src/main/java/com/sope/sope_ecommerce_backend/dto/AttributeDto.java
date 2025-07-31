package com.sope.sope_ecommerce_backend.dto;

import lombok.Data;

@Data
public class AttributeDto {
      private String name;
      private String value;
      private Long imageId;

      // Getters and setters
      public String getName() {
            return name;
      }

      public void setName(String name) {
            this.name = name;
      }

      public String getValue() {
            return value;
      }

      public void setValue(String value) {
            this.value = value;
      }

      public Long getImageId() {
            return imageId;
      }

      public void setImageId(Long imageId) {
            this.imageId = imageId;
      }
}
