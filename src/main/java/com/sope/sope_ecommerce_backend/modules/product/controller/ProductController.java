package com.sope.sope_ecommerce_backend.modules.product.controller;

import com.sope.sope_ecommerce_backend.modules.product.dto.ProductCreateRequest;
import com.sope.sope_ecommerce_backend.modules.product.dto.ProductResponse;
import com.sope.sope_ecommerce_backend.modules.product.dto.ProductVariantCreateRequest;
import com.sope.sope_ecommerce_backend.modules.product.dto.ProductVariantResponse;
import com.sope.sope_ecommerce_backend.modules.product.entity.ProductDetail;
import com.sope.sope_ecommerce_backend.modules.product.service.ProductService;
import com.sope.sope_ecommerce_backend.modules.product.service.ProductVariantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import com.sope.sope_ecommerce_backend.modules.product.enums.StatusProduct;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
      private final ProductService productService;
      private final ProductVariantService productVariantService;

      @Autowired
      public ProductController(ProductService productService, ProductVariantService productVariantService) {
            this.productService = productService;
            this.productVariantService = productVariantService;
      }

      // CREATE PRODUCT
      @PostMapping(consumes = { "multipart/form-data" })
      public ResponseEntity<ProductResponse> createProduct(
                  @RequestParam("name") String name,
                  @RequestParam("defaultPrice") BigDecimal defaultPrice,
                  @RequestParam("brand") String brand,
                  @RequestParam("description") String description,
                  @RequestParam("categoryId") UUID categoryId,
                  @RequestParam("shopId") UUID shopId,
                  @RequestParam("hidden") boolean hidden,
                  @RequestPart("defaultImage") MultipartFile imageFile) {
            ProductCreateRequest request = new ProductCreateRequest();
            request.setName(name);
            request.setDefaultPrice(defaultPrice);
            request.setBrand(brand);
            request.setDescription(description);
            request.setCategoryId(categoryId);
            request.setShopId(shopId);
            request.setHidden(hidden);
            request.setStatus(StatusProduct.PENDING); // Luôn là PENDING khi tạo mới

            ProductResponse newProduct = productService.createProduct(request, imageFile);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
      }

      // PRODUCT BY CATEGORY
      @GetMapping("/by-category/{slug}")
      public ResponseEntity<List<ProductResponse>> getProductsByCategorySlug(@PathVariable String slug) {
            List<ProductResponse> products = productService.getProductsByCategorySlug(slug);
            return ResponseEntity.ok(products);
      }

      // CREATE PRODUCT VARIANT
      @PostMapping("/variants")
      public ResponseEntity<ProductVariantResponse> createProductVariant(
                  @RequestBody ProductVariantCreateRequest request) {
            ProductVariantResponse response = productVariantService.createProductVariant(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
      }

      @GetMapping("/by-slug/{slug}")
      public ResponseEntity<ProductResponse> getProductBySlug(@PathVariable String slug) {
            ProductResponse response = productService.getProductBySlug(slug);
            return ResponseEntity.ok(response);
      }

      @PostMapping("/create-details")
      public ResponseEntity<ProductDetail> createProductDetail(
                  @PathVariable UUID productId,
                  @RequestBody ProductDetail detail) {
            ProductDetail created = productService.createProductDetail(productId, detail);
            return ResponseEntity.ok(created);
      }

      @GetMapping("/get-details")
      public ResponseEntity<List<ProductDetail>> getProductDetails(@PathVariable UUID productId) {
            List<ProductDetail> details = productService.getProductDetailsByProductId(productId);
            return ResponseEntity.ok(details);
      }

}
