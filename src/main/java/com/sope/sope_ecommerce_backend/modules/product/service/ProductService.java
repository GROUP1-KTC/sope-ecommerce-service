package com.sope.sope_ecommerce_backend.modules.product.service;

import com.github.slugify.Slugify;
import com.sope.sope_ecommerce_backend.modules.product.dto.ProductCreateRequest;
import com.sope.sope_ecommerce_backend.modules.product.dto.ProductResponse;
import com.sope.sope_ecommerce_backend.modules.product.entity.Category;
import com.sope.sope_ecommerce_backend.modules.product.entity.Product;
import com.sope.sope_ecommerce_backend.modules.product.entity.ProductDetail;
import com.sope.sope_ecommerce_backend.modules.product.entity.ProductVariant;
import com.sope.sope_ecommerce_backend.modules.product.repository.CategoryRepository;
import com.sope.sope_ecommerce_backend.modules.product.repository.ProductDetailRepository;
import com.sope.sope_ecommerce_backend.modules.product.repository.ProductRepository;
import com.sope.sope_ecommerce_backend.modules.product.repository.ProductVariantRepository;
import com.sope.sope_ecommerce_backend.modules.shop.entity.Shop;
import com.sope.sope_ecommerce_backend.modules.shop.repository.ShopRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sope.sope_ecommerce_backend.modules.product.dto.ProductVariantResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class ProductService {

      private final ProductRepository productRepository;
      private final CategoryRepository categoryRepository;
      private final CategoryService categoryService;
      private final ShopRepository shopRepository;
      private final Slugify slugify;
      private final Cloudinary cloudinary;
      private final ProductVariantRepository productVariantRepository;
      private final ProductVariantService productVariantService;
      private final ProductDetailRepository productDetailRepository;

      @Autowired
      public ProductService(ProductRepository productRepository,
                  CategoryRepository categoryRepository,
                  ShopRepository shopRepository,
                  Cloudinary cloudinary,
                  ProductVariantRepository productVariantRepository,
                  ProductVariantService productVariantService,
                  ProductDetailRepository productDetailRepository,
                  CategoryService categoryService) {
            this.productRepository = productRepository;
            this.categoryRepository = categoryRepository;
            this.shopRepository = shopRepository;
            this.slugify = Slugify.builder().build();
            this.cloudinary = cloudinary;
            this.productVariantRepository = productVariantRepository;
            this.productVariantService = productVariantService;
            this.productDetailRepository = productDetailRepository;
            this.categoryService = categoryService;

      }

      public List<ProductResponse> getProductsByCategorySlug(String slug) {
            Category rootCategory = categoryRepository.findBySlug(slug)
                        .orElseThrow(() -> new EntityNotFoundException("Category not found with slug: " + slug));

            List<Category> categories = categoryService.getAllDescendantCategories(rootCategory);
            List<Product> products = productRepository.findByCategoryIn(categories);

            return products.stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList());
      }

      @Transactional
      public ProductResponse createProduct(ProductCreateRequest request, MultipartFile imageFile) {
            Category category = categoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                    "Category not found with id: " + request.getCategoryId()));

            Shop shop = shopRepository.findById(request.getShopId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                    "Shop not found with id: " + request.getShopId()));

            String imageUrl = null;
            if (imageFile != null && !imageFile.isEmpty()) {
                  try {
                        Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
                        imageUrl = (String) uploadResult.get("secure_url");
                  } catch (Exception e) {
                        throw new RuntimeException("Failed to upload image", e);
                  }
            }

            Product product = new Product();
            product.setName(request.getName());
            product.setDefaultPrice(request.getDefaultPrice());
            product.setBrand(request.getBrand());
            product.setDescription(request.getDescription());
            product.setDefaultImage(imageUrl);
            product.setHidden(request.isHidden());
            product.setStatus(request.getStatus());
            product.setCategory(category);
            product.setShop(shop);
            product.setCreatedAt(LocalDateTime.now());

            // Generate and set slug
            String slug = slugify.slugify(request.getName() + "-" + System.currentTimeMillis());
            product.setSlug(slug);

            Product savedProduct = productRepository.save(product);

            return toResponse(savedProduct);
      }

      public List<ProductResponse> getAllProducts() {
            return productRepository.findAll()
                        .stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList());
      }

      public ProductResponse getProductById(UUID id) {
            Product product = productRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Product not found"));
            ProductResponse response = toResponse(product);

            // Lấy danh sách variants
            List<ProductVariant> variants = productVariantRepository.findByProduct_ProductId(id);
            List<ProductVariantResponse> variantResponses = variants.stream()
                        .map(variant -> productVariantService.toResponse(variant))
                        .collect(Collectors.toList());
            response.setVariants(variantResponses);

            return response;
      }

      public ProductResponse getProductBySlug(String slug) {
            Product product = productRepository.findBySlug(slug)
                        .orElseThrow(() -> new EntityNotFoundException("Product not found with slug: " + slug));

            ProductResponse response = toResponse(product);

            // Lấy danh sách variants
            List<ProductVariant> variants = productVariantRepository.findByProduct_ProductId(product.getProductId());
            List<ProductVariantResponse> variantResponses = variants.stream()
                        .map(variant -> productVariantService.toResponse(variant))
                        .collect(Collectors.toList());

            response.setVariants(variantResponses);
            return response;
      }

      public ProductDetail createProductDetail(UUID productId, ProductDetail detail) {
            Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new EntityNotFoundException("Product not found"));
            detail.setProduct(product);
            return productDetailRepository.save(detail);
      }

      public List<ProductDetail> getProductDetailsByProductId(UUID productId) {
            return productDetailRepository.findByProduct_ProductId(productId);
      }

      private ProductResponse toResponse(Product product) {
            ProductResponse response = new ProductResponse();
            response.setProductId(product.getProductId());
            response.setName(product.getName());
            response.setDefaultPrice(product.getDefaultPrice());
            response.setBrand(product.getBrand());
            response.setDescription(product.getDescription());
            response.setDefaultImage(product.getDefaultImage());
            response.setHidden(product.isHidden());
            response.setStatus(product.getStatus());
            response.setSlug(product.getSlug());
            response.setCreatedAt(product.getCreatedAt());

            ProductResponse.CategoryInfo categoryInfo = new ProductResponse.CategoryInfo();
            categoryInfo.setId(product.getCategory().getId());
            categoryInfo.setName(product.getCategory().getName());
            response.setCategory(categoryInfo);

            ProductResponse.ShopInfo shopInfo = new ProductResponse.ShopInfo();
            shopInfo.setName(product.getShop().getName());
            response.setShop(shopInfo);

            return response;
      }
}