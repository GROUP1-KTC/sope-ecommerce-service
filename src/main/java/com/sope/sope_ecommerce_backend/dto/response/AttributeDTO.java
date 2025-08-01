package com.sope.sope_ecommerce_backend.dto.response;

import lombok.Data;

@Data
public class AttributeDTO {
      private String name;
      private String value;

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

}
