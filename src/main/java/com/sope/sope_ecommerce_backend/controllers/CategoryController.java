package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.request.CategoryCreateDTO;
import com.sope.sope_ecommerce_backend.dto.response.CategoryDTO;
import com.sope.sope_ecommerce_backend.services.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
      @Autowired
      private CategoryServiceImpl categoryService;

      @PostMapping
      public CategoryDTO createCategory(@RequestBody CategoryCreateDTO request) {
            return categoryService.createCategory(request);
      }

      @GetMapping
      public List<CategoryDTO> getAllCategories() {
            return categoryService.getAllCategories();
      }
}