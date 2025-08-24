package com.sope.sope_ecommerce_backend.mapper;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultipartFileMapper {
      public String map(MultipartFile file) {
            if (file == null)
                  return null;
            return file.getOriginalFilename();
      }
}