package com.sope.sope_ecommerce_backend.services;

import java.util.List;

import com.sope.sope_ecommerce_backend.dto.request.CategoryCreateDTO;
import com.sope.sope_ecommerce_backend.dto.response.CategoryDTO;

public interface CategoryService {
      CategoryDTO createCategory(CategoryCreateDTO request);

      List<CategoryDTO> getAllCategories();
}
