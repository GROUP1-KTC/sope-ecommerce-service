//package com.sope.sope_ecommerce_backend.controllers;
//
//import com.sope.sope_ecommerce_backend.dto.request.ProductCreateDTO;
//import com.sope.sope_ecommerce_backend.dto.response.ProductDTO;
//import com.sope.sope_ecommerce_backend.dto.request.ProductVariantCreateDTO;
//import com.sope.sope_ecommerce_backend.dto.response.ProductVariantDTO;
//import com.sope.sope_ecommerce_backend.entities.ProductDetailEntity;
//import com.sope.sope_ecommerce_backend.services.impl.ProductServiceImpl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.UUID;
//import org.springframework.web.multipart.MultipartFile;
//import com.sope.sope_ecommerce_backend.enums.StatusProduct;
//
//import java.math.BigDecimal;
//
//@RestController
//@RequestMapping("/api/v1/products")
//public class ProductController {
//      private final ProductServiceImpl productService;
//      private final ProductVariantService productVariantService;
//
//      @Autowired
//      public ProductController(ProductServiceImpl productService, ProductVariantService productVariantService) {
//            this.productService = productService;
//            this.productVariantService = productVariantService;
//      }
//
//      // CREATE PRODUCT
//      @PostMapping(consumes = { "multipart/form-data" })
//      public ResponseEntity<ProductDTO> createProduct(
//                  @RequestParam("name") String name,
//                  @RequestParam("defaultPrice") BigDecimal defaultPrice,
//                  @RequestParam("brand") String brand,
//                  @RequestParam("description") String description,
//                  @RequestParam("categoryId") UUID categoryId,
//                  @RequestParam("shopId") UUID shopId,
//                  @RequestParam("hidden") boolean hidden,
//                  @RequestPart("defaultImage") MultipartFile imageFile) {
//            ProductCreateDTO request = new ProductCreateDTO();
//            request.setName(name);
//            request.setDefaultPrice(defaultPrice);
//            request.setBrand(brand);
//            request.setDescription(description);
//            request.setCategoryId(categoryId);
//            request.setShopId(shopId);
//            request.setHidden(hidden);
//            request.setStatus(StatusProduct.PENDING); // Luôn là PENDING khi tạo mới
//
//            ProductDTO newProduct = productService.createProduct(request, imageFile);
//            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
//      }
//
//      // PRODUCT BY CATEGORY
//      @GetMapping("/by-category/{slug}")
//      public ResponseEntity<List<ProductDTO>> getProductsByCategorySlug(@PathVariable String slug) {
//            List<ProductDTO> products = productService.getProductsByCategorySlug(slug);
//            return ResponseEntity.ok(products);
//      }
//
//      // CREATE PRODUCT VARIANT
//      @PostMapping("/variants")
//      public ResponseEntity<ProductVariantDTO> createProductVariant(
//                  @RequestBody ProductVariantCreateDTO request) {
//            ProductVariantDTO response = productVariantService.createProductVariant(request);
//            return new ResponseEntity<>(response, HttpStatus.CREATED);
//      }
//
//      @GetMapping("/by-slug/{slug}")
//      public ResponseEntity<ProductDTO> getProductBySlug(@PathVariable String slug) {
//            ProductDTO response = productService.getProductBySlug(slug);
//            return ResponseEntity.ok(response);
//      }
//
//      @PostMapping("/create-details")
//      public ResponseEntity<ProductDetailEntity> createProductDetail(
//                  @PathVariable UUID productId,
//                  @RequestBody ProductDetailEntity detail) {
//            ProductDetailEntity created = productService.createProductDetail(productId, detail);
//            return ResponseEntity.ok(created);
//      }
//
//      @GetMapping("/get-details")
//      public ResponseEntity<List<ProductDetailEntity>> getProductDetails(@PathVariable UUID productId) {
//            List<ProductDetailEntity> details = productService.getProductDetailsByProductId(productId);
//            return ResponseEntity.ok(details);
//      }
//
//}
