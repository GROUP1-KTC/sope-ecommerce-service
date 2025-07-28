package com.sope.sope_ecommerce_backend.modules.product.controller;

import com.sope.sope_ecommerce_backend.modules.product.dto.CategoryCreateRequest;
import com.sope.sope_ecommerce_backend.modules.product.dto.CategoryResponse;
import com.sope.sope_ecommerce_backend.modules.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
      @Autowired
      private CategoryService categoryService;

      @PostMapping
      public CategoryResponse createCategory(@RequestBody CategoryCreateRequest request) {
            return categoryService.createCategory(request);
      }

      @GetMapping
      public List<CategoryResponse> getAllCategories() {
            return categoryService.getAllCategories();
      }
}