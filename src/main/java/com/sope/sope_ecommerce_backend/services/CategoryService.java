package com.sope.sope_ecommerce_backend.services;

import java.util.List;
import java.util.UUID;

// import org.springframework.web.bind.annotation.PathVariable;

import com.sope.sope_ecommerce_backend.dto.request.CategoryCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.CategoryUpdateCommissionDTO;
import com.sope.sope_ecommerce_backend.dto.response.CategoryDTO;
// import com.sope.sope_ecommerce_backend.dto.response.CategoryTreeDTO;

public interface CategoryService {
      CategoryDTO createCategory(CategoryCreateDTO request);

      List<CategoryDTO> getBreadcrumb(UUID categoryId);

      List<CategoryDTO> getAllCategories();

      // CategoryTreeDTO getCategoryTree(@PathVariable UUID id);

      CategoryDTO updateCommissionFee(UUID categoryId, CategoryUpdateCommissionDTO request);

}
