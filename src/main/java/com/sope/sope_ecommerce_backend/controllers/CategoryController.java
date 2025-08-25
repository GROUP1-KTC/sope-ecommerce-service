package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.request.CategoryCreateDTO;
import com.sope.sope_ecommerce_backend.dto.response.CategoryDTO;
import com.sope.sope_ecommerce_backend.services.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
      @Autowired
      private CategoryServiceImpl categoryService;

      @PostMapping
      public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryCreateDTO request) {
            CategoryDTO createCategory = categoryService.createCategory(request);
            return ResponseEntity.ok(createCategory);
      }

      @GetMapping
      public ResponseEntity<List<CategoryDTO>> getAllCategories() {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
      }
}