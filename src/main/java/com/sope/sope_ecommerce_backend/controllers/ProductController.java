package com.sope.sope_ecommerce_backend.controllers;

import java.util.List;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.dto.response.*;
import com.sope.sope_ecommerce_backend.enums.StatusProduct;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sope.sope_ecommerce_backend.dto.request.ProductCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ProductUpdateDTO;
import com.sope.sope_ecommerce_backend.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
      private final ProductService productService;

      @GetMapping("/slug/{slug}")
      public ResponseEntity<ProductDTO> getProductBySlug(@PathVariable String slug) {
            ProductDTO products = productService.getProductBySlug(slug);
            return ResponseEntity.ok(products);
      }

      @GetMapping("/init")
      public List<ProductSummaryResponse> getInitProducts() {
            return productService.getInitProducts();
      }

      @GetMapping("/initforguest")
      public List<ProductSummaryResponse> getInitProductsForGuest() {
            return productService.getInitProductsForGuest();
      }

      @GetMapping("/suggested/{productId}")
      public ResponseEntity<List<ProductSummaryResponse>> getSuggestedProducts(
                  @PathVariable UUID productId,
                  @RequestParam(defaultValue = "10") int limit) {
            List<ProductSummaryResponse> suggested = productService.getSuggestedProducts(productId, limit);
            return ResponseEntity.ok(suggested);
      }

      @GetMapping("/similar/{productId}")
      public ResponseEntity<List<ProductSummaryResponse>> getSimilarProducts(
                  @PathVariable UUID productId,
                  @RequestParam(defaultValue = "10") int limit) {

            List<ProductSummaryResponse> similar = productService.getSimilarProducts(productId, limit);
            return ResponseEntity.ok(similar);
      }

      @GetMapping("/approved")
      public ResponseEntity<Page<ProductDTO>> getApprovedProducts(
                  @RequestParam(defaultValue = "0") int page,
                  @RequestParam(defaultValue = "12") int size) {
            return ResponseEntity.ok(productService.getApprovedProducts(page, size));
      }

      @PatchMapping("/{productId}/status")
      public ResponseEntity<ProductDTO> updateProductStatus(
                  @PathVariable UUID productId,
                  @RequestParam StatusProduct status) {
            ProductDTO updated = productService.updateProductStatus(productId, status);
            return ResponseEntity.ok(updated);
      }

      @GetMapping
      public ResponseEntity<List<ProductDTO>> getAllProducts() {
            return ResponseEntity.ok(productService.getAllProducts());
      }

      @GetMapping("/shop")
      public ResponseEntity<Page<ProductDTO>> getProductsByShop(
                  @RequestParam(defaultValue = "0") int page,
                  @RequestParam(defaultValue = "12") int size) {
            return ResponseEntity.ok(productService.getProductsByShop(page, size));
      }

      @GetMapping("/shop/{shopId}/approved")
      public ResponseEntity<Page<ProductSummaryResponse>> getApprovedProductsByShop(
                  @PathVariable UUID shopId,
                  @RequestParam(defaultValue = "0") int page,
                  @RequestParam(defaultValue = "12") int size) {
            return ResponseEntity.ok(productService.getApprovedProductsByShop(shopId, page, size));
      }

      @GetMapping("/by-category/slug/{slug}")
      public ResponseEntity<Page<ProductSummaryResponse>> getProductsByCategorySlug(
                  @PathVariable String slug,
                  @RequestParam(defaultValue = "0") int page,
                  @RequestParam(defaultValue = "12") int size) {
            return ResponseEntity.ok(productService.getProductsByCategoryIncludingChildren(slug, page, size));
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
                  @RequestPart(value = "defaultImage", required = false) MultipartFile defaultImage,
                  @RequestPart(value = "defaultVideoIntro", required = false) MultipartFile defaultVideoIntro,
                  @RequestPart(value = "productImages", required = false) List<MultipartFile> productImages,
                  @RequestPart(value = "variantFiles", required = false) List<MultipartFile> variantFiles) {

            return productService.updateProduct(
                        slug,
                        productUpdateDTO,
                        defaultImage,
                        defaultVideoIntro,
                        productImages,
                        variantFiles);
      }

}
