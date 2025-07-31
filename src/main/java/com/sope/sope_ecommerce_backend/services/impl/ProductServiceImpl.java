//package com.sope.sope_ecommerce_backend.services.impl;
//
//import com.github.slugify.Slugify;
//import com.sope.sope_ecommerce_backend.dto.request.ProductCreateDTO;
//import com.sope.sope_ecommerce_backend.dto.response.ProductDTO;
//import com.sope.sope_ecommerce_backend.entities.CategoryEntity;
//import com.sope.sope_ecommerce_backend.entities.ProductEntity;
//import com.sope.sope_ecommerce_backend.entities.ProductDetailEntity;
//import com.sope.sope_ecommerce_backend.entities.ProductVariantEntity;
//import com.sope.sope_ecommerce_backend.repositories.CategoryRepository;
//import com.sope.sope_ecommerce_backend.repositories.ProductDetailRepository;
//import com.sope.sope_ecommerce_backend.repositories.ProductRepository;
//import com.sope.sope_ecommerce_backend.repositories.ProductVariantRepository;
//import com.sope.sope_ecommerce_backend.modules.product.service.ProductVariantService;
//import com.sope.sope_ecommerce_backend.entities.ShopEntity;
//import com.sope.sope_ecommerce_backend.repositories.ShopRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import com.sope.sope_ecommerce_backend.dto.response.ProductVariantDTO;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Map;
//
//@Service
//public class ProductServiceImpl {
//
//      private final ProductRepository productRepository;
//      private final CategoryRepository categoryRepository;
//      private final CategoryServiceImpl categoryService;
//      private final ShopRepository shopRepository;
//      private final Slugify slugify;
//      private final Cloudinary cloudinary;
//      private final ProductVariantRepository productVariantRepository;
//      private final ProductVariantService productVariantService;
//      private final ProductDetailRepository productDetailRepository;
//
//      @Autowired
//      public ProductServiceImpl(ProductRepository productRepository,
//                                CategoryRepository categoryRepository,
//                                ShopRepository shopRepository,
//                                Cloudinary cloudinary,
//                                ProductVariantRepository productVariantRepository,
//                                ProductVariantService productVariantService,
//                                ProductDetailRepository productDetailRepository,
//                                CategoryServiceImpl categoryService) {
//            this.productRepository = productRepository;
//            this.categoryRepository = categoryRepository;
//            this.shopRepository = shopRepository;
//            this.slugify = Slugify.builder().build();
//            this.cloudinary = cloudinary;
//            this.productVariantRepository = productVariantRepository;
//            this.productVariantService = productVariantService;
//            this.productDetailRepository = productDetailRepository;
//            this.categoryService = categoryService;
//
//      }
//
//      public List<ProductDTO> getProductsByCategorySlug(String slug) {
//            CategoryEntity rootCategory = categoryRepository.findBySlug(slug)
//                        .orElseThrow(() -> new EntityNotFoundException("Category not found with slug: " + slug));
//
//            List<CategoryEntity> categories = categoryService.getAllDescendantCategories(rootCategory);
//            List<ProductEntity> products = productRepository.findByCategoryIn(categories);
//
//            return products.stream()
//                        .map(this::toResponse)
//                        .collect(Collectors.toList());
//      }
//
//      @Transactional
//      public ProductDTO createProduct(ProductCreateDTO request, MultipartFile imageFile) {
//            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
//                        .orElseThrow(() -> new EntityNotFoundException(
//                                    "Category not found with id: " + request.getCategoryId()));
//
//            ShopEntity shop = shopRepository.findById(request.getShopId())
//                        .orElseThrow(() -> new EntityNotFoundException(
//                                    "Shop not found with id: " + request.getShopId()));
//
//            String imageUrl = null;
//            if (imageFile != null && !imageFile.isEmpty()) {
//                  try {
//                        Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
//                        imageUrl = (String) uploadResult.get("secure_url");
//                  } catch (Exception e) {
//                        throw new RuntimeException("Failed to upload image", e);
//                  }
//            }
//
//            ProductEntity product = new ProductEntity();
//            product.setName(request.getName());
//            product.setDefaultPrice(request.getDefaultPrice());
//            product.setBrand(request.getBrand());
//            product.setDescription(request.getDescription());
//            product.setDefaultImage(imageUrl);
//            product.setHidden(request.isHidden());
//            product.setStatus(request.getStatus());
//            product.setCategory(category);
//            product.setShop(shop);
//            product.setCreatedAt(LocalDateTime.now());
//
//            // Generate and set slug
//            String slug = slugify.slugify(request.getName() + "-" + System.currentTimeMillis());
//            product.setSlug(slug);
//
//            ProductEntity savedProduct = productRepository.save(product);
//
//            return toResponse(savedProduct);
//      }
//
//      public List<ProductDTO> getAllProducts() {
//            return productRepository.findAll()
//                        .stream()
//                        .map(this::toResponse)
//                        .collect(Collectors.toList());
//      }
//
//      public ProductDTO getProductById(UUID id) {
//            ProductEntity product = productRepository.findById(id)
//                        .orElseThrow(() -> new EntityNotFoundException("Product not found"));
//            ProductDTO response = toResponse(product);
//
//            // Lấy danh sách variants
//            List<ProductVariantEntity> variants = productVariantRepository.findByProduct_ProductId(id);
//            List<ProductVariantDTO> variantResponses = variants.stream()
//                        .map(variant -> productVariantService.toResponse(variant))
//                        .collect(Collectors.toList());
//            response.setVariants(variantResponses);
//
//            return response;
//      }
//
//      public ProductDTO getProductBySlug(String slug) {
//            ProductEntity product = productRepository.findBySlug(slug)
//                        .orElseThrow(() -> new EntityNotFoundException("Product not found with slug: " + slug));
//
//            ProductDTO response = toResponse(product);
//
//            // Lấy danh sách variants
//            List<ProductVariantEntity> variants = productVariantRepository.findByProduct_ProductId(product.getProductId());
//            List<ProductVariantDTO> variantResponses = variants.stream()
//                        .map(variant -> productVariantService.toResponse(variant))
//                        .collect(Collectors.toList());
//
//            response.setVariants(variantResponses);
//            return response;
//      }
//
//      public ProductDetailEntity createProductDetail(UUID productId, ProductDetailEntity detail) {
//            ProductEntity product = productRepository.findById(productId)
//                        .orElseThrow(() -> new EntityNotFoundException("Product not found"));
//            detail.setProduct(product);
//            return productDetailRepository.save(detail);
//      }
//
//      public List<ProductDetailEntity> getProductDetailsByProductId(UUID productId) {
//            return productDetailRepository.findByProduct_ProductId(productId);
//      }
//
//      private ProductDTO toResponse(ProductEntity product) {
//            ProductDTO response = new ProductDTO();
//            response.setProductId(product.getProductId());
//            response.setName(product.getName());
//            response.setDefaultPrice(product.getDefaultPrice());
//            response.setBrand(product.getBrand());
//            response.setDescription(product.getDescription());
//            response.setDefaultImage(product.getDefaultImage());
//            response.setHidden(product.isHidden());
//            response.setStatus(product.getStatus());
//            response.setSlug(product.getSlug());
//            response.setCreatedAt(product.getCreatedAt());
//
//            ProductDTO.CategoryInfo categoryInfo = new ProductDTO.CategoryInfo();
//            categoryInfo.setId(product.getCategory().getId());
//            categoryInfo.setName(product.getCategory().getName());
//            response.setCategory(categoryInfo);
//
//            ProductDTO.ShopInfo shopInfo = new ProductDTO.ShopInfo();
//            shopInfo.setName(product.getShop().getName());
//            response.setShop(shopInfo);
//
//            return response;
//      }
//}