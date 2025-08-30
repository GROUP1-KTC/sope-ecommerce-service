package com.sope.sope_ecommerce_backend.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sope.sope_ecommerce_backend.dto.request.ProductCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ProductUpdateDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductBasicWithVariantsDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductByCategory;
import com.sope.sope_ecommerce_backend.dto.response.ProductDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductVariantDetailDTO;
import com.sope.sope_ecommerce_backend.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
      private final ProductService productService;

      @GetMapping("/slug/{slug}")
      public ResponseEntity<ProductDTO> getProductBySlug(@PathVariable String slug) {
            ProductDTO products = productService.getProductBySlug(slug);
            return ResponseEntity.ok(products);
      }

      // @GetMapping("/by-category/{categoryId}")
      // public ResponseEntity<List<ProductByCategory>>
      // getProductsByCategory(@PathVariable UUID categoryId) {
      // List<ProductByCategory> products =
      // productService.getProductsByCategoryIncludingChildren(categoryId);
      // return ResponseEntity.ok(products);
      // }

      @GetMapping("/by-category/slug/{slug}")
      public ResponseEntity<List<ProductByCategory>> getProductsByCategorySlug(@PathVariable String slug) {
            return ResponseEntity.ok(productService.getProductsByCategoryIncludingChildren(slug));
      }

      @GetMapping("/{productId}/with-variants")
      public ResponseEntity<ProductBasicWithVariantsDTO> getProductWithVariants(@PathVariable UUID productId) {
            ProductBasicWithVariantsDTO response = productService.getProductWithVariants(productId);
            return ResponseEntity.ok(response);
      }

      @GetMapping("/{productVariantId}")
      public ResponseEntity<ProductVariantDetailDTO> getProductVariantDetail(@PathVariable UUID productVariantId) {
            ProductVariantDetailDTO detailDTO = productService.getProductVariantDetail(productVariantId);
            return ResponseEntity.ok(detailDTO);
      }

      @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
      public ResponseEntity<ProductDTO> createProduct(
                  @RequestPart("product") ProductCreateDTO productCreateDTO,
                  @RequestPart("defaultImage") MultipartFile defaultImage,
                  @RequestPart(value = "defaultVideoIntro", required = false) MultipartFile defaultVideoIntro,
                  @RequestPart(value = "productImages", required = false) List<MultipartFile> productImages,
                  @RequestPart(value = "variantFiles", required = false) List<MultipartFile> variantFiles) {

            ProductDTO createProduct = productService.createProduct(
                        productCreateDTO,
                        defaultImage,
                        defaultVideoIntro,
                        productImages,
                        variantFiles);
            return ResponseEntity.ok(createProduct);
      }

      @PatchMapping(value = "/{slug}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
      public ProductDTO updateProduct(
                  @PathVariable String slug,
                  @RequestPart("product") ProductUpdateDTO productUpdateDTO,
                  @RequestPart(value = "defaultVideoIntro", required = false) MultipartFile defaultVideoIntro,
                  @RequestPart(value = "productImages", required = false) List<MultipartFile> productImages,
                  @RequestPart(value = "variantFiles", required = false) List<MultipartFile> variantFiles) {

            return productService.updateProduct(
                        slug,
                        productUpdateDTO,
                        defaultVideoIntro,
                        productImages,
                        variantFiles);
      }

}
