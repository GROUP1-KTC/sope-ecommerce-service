package com.sope.sope_ecommerce_backend.controllers;

import com.sope.sope_ecommerce_backend.dto.request.CategoryCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.CategoryUpdateCommissionDTO;
import com.sope.sope_ecommerce_backend.dto.response.CategoryDTO;
import com.sope.sope_ecommerce_backend.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
      private final CategoryService categoryService;

      @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
      public ResponseEntity<CategoryDTO> createCategory(
                  @RequestPart("category") CategoryCreateDTO request,
                  @RequestPart(value = "imageForParent", required = false) MultipartFile imageForParent) {
            CategoryDTO createCategory = categoryService.createCategory(request, imageForParent);
            return ResponseEntity.ok(createCategory);
      }

      @GetMapping
      public ResponseEntity<List<CategoryDTO>> getAllCategories() {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(categories);
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