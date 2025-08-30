package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.request.CategoryCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.CategoryUpdateCommissionDTO;
import com.sope.sope_ecommerce_backend.dto.response.CategoryDTO;
import com.sope.sope_ecommerce_backend.dto.response.CategoryTreeDTO;
import com.sope.sope_ecommerce_backend.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
      private final CategoryService categoryService;

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

      @GetMapping("/categories/{id}/tree")
      public ResponseEntity<CategoryTreeDTO> getCategoryTree(@PathVariable UUID id) {
            return ResponseEntity.ok(categoryService.getCategoryTree(id));
      }

      @GetMapping("/{id}/breadcrumb")
      public ResponseEntity<List<CategoryDTO>> getBreadcrumb(@PathVariable UUID id) {
            List<CategoryDTO> breadcrumb = categoryService.getBreadcrumb(id);
            return ResponseEntity.ok(breadcrumb);
      }

      @PatchMapping("/{id}/fee-commission")
      public ResponseEntity<CategoryDTO> updateCommissionFee(
                  @PathVariable UUID id,
                  @RequestBody CategoryUpdateCommissionDTO request) {
            CategoryDTO updated = categoryService.updateCommissionFee(id, request);
            return ResponseEntity.ok(updated);
      }
}