package com.sope.sope_ecommerce_backend.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sope.sope_ecommerce_backend.dto.request.ProductCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ProductUpdateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ProductVariantCreateDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductDTO;
import com.sope.sope_ecommerce_backend.entities.ProductDetailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.sope.sope_ecommerce_backend.services.impl.ProductServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import com.sope.sope_ecommerce_backend.enums.StatusProduct;

import io.swagger.v3.oas.annotations.Parameter;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductServiceImpl productService;

    @Autowired
    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<ProductDTO> createProduct(
            @RequestParam("name") String name,
            @RequestParam("defaultPrice") BigDecimal defaultPrice,
            @RequestParam("brand") String brand,
            @RequestParam("description") String description,
            @RequestParam("categoryId") UUID categoryId,
            @RequestParam("shopId") UUID shopId,
            @RequestParam("hidden") boolean hidden,
            @RequestParam("stock") int stock,
            @RequestParam("sold") int sold,
            @RequestParam("defaultImage") MultipartFile defaultImageFile,
            @RequestParam(value = "imagesList", required = false) List<MultipartFile> imagesList,
            @RequestParam(value = "defaultVideoIntro", required = false) MultipartFile defaultVideoIntroFile,
            @RequestPart(value = "variants", required = false) @Parameter(description = "JSON array of variants") String variantsJson,
            @RequestParam(required = false) java.util.Map<String, MultipartFile> imageVariants) {
        ProductCreateDTO request = new ProductCreateDTO();
        request.setName(name);
        request.setDefaultPrice(defaultPrice);
        request.setBrand(brand);
        request.setDescription(description);
        request.setCategoryId(categoryId);
        request.setShopId(shopId);
        request.setHidden(hidden);
        request.setStock(stock);
        request.setSold(sold);
        request.setStatus(StatusProduct.PENDING); // Luôn là PENDING khi tạo mới

        List<ProductVariantCreateDTO> variants = new ArrayList<>();
        if (variantsJson != null && !variantsJson.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                TypeReference<List<ProductVariantCreateDTO>> typeRef = new TypeReference<>() {
                };
                variants = objectMapper.readValue(variantsJson, typeRef);
            } catch (Exception e) {
                throw new RuntimeException("Invalid variants JSON", e);
            }
        }

        for (int i = 0; i < variants.size(); i++) {
            String key = "imageVariant-" + i;
            if (imageVariants != null && imageVariants.containsKey(key)) {
                variants.get(i).setImageVariant(key);
            }
        }

        ProductDTO newProduct = productService.createProduct(
                request, defaultImageFile, imagesList, defaultVideoIntroFile, variants, imageVariants);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    // GET ALL PRODUCTS
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(
            @RequestParam(defaultValue = "false") boolean includeHidden) {
        List<ProductDTO> products = productService.getAllProducts(includeHidden);
        return ResponseEntity.ok(products);
    }

    // PRODUCT BY SLUG
    @GetMapping("/by-slug/{slug}")
    public ResponseEntity<ProductDTO> getProductBySlug(@PathVariable String slug) {
        ProductDTO product = productService.getProductBySlug(slug);
        return ResponseEntity.ok(product);
    }

    // GET PRODUCT BY ID
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID productId) {
        ProductDTO product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    // UPDATE PRODUCT
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable UUID productId,
            @RequestBody ProductUpdateDTO request) {
        ProductDTO updatedProduct = productService.updateProduct(productId, request);
        return ResponseEntity.ok(updatedProduct);
    }

    @PostMapping("/create-details")
    public ResponseEntity<ProductDetailEntity> createProductDetail(
            @PathVariable UUID productId,
            @RequestBody ProductDetailEntity detail) {
        ProductDetailEntity created = productService.createProductDetail(productId, detail);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/get-details")
    public ResponseEntity<List<ProductDetailEntity>> getProductDetails(@PathVariable UUID productId) {
        List<ProductDetailEntity> details = productService.getProductDetailsByProductId(productId);
        return ResponseEntity.ok(details);
    }

}
