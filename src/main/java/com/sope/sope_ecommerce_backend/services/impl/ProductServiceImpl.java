package com.sope.sope_ecommerce_backend.services.impl;

import com.github.slugify.Slugify;

import com.sope.sope_ecommerce_backend.dto.request.ProductCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ProductUpdateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ProductVariantCreateDTO;
import com.sope.sope_ecommerce_backend.dto.response.AttributeDTO;
import com.sope.sope_ecommerce_backend.dto.response.ImageDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductDTO;
import com.sope.sope_ecommerce_backend.repositories.CategoryRepository;
import com.sope.sope_ecommerce_backend.repositories.ProductDetailRepository;
import com.sope.sope_ecommerce_backend.repositories.ProductRepository;
import com.sope.sope_ecommerce_backend.repositories.ProductVariantRepository;
import com.sope.sope_ecommerce_backend.repositories.AttributeRepository;
import com.sope.sope_ecommerce_backend.repositories.ShopRepository;
import com.sope.sope_ecommerce_backend.entities.*;
import com.sope.sope_ecommerce_backend.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sope.sope_ecommerce_backend.dto.response.ProductVariantDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

@Service
public class ProductServiceImpl {
      private final ProductRepository productRepository;
      private final CategoryRepository categoryRepository;

      // private final CategoryService categoryService;
      private final ShopRepository shopRepository;
      private final Slugify slugify;
      private final Cloudinary cloudinary;
      private final ProductVariantRepository productVariantRepository;
      private final ProductDetailRepository productDetailRepository;
      private final AttributeRepository attributeRepository;

      @Autowired
      public ProductServiceImpl(ProductRepository productRepository,
                            CategoryRepository categoryRepository,
                            ShopRepository shopRepository,
                            Cloudinary cloudinary,
                            ProductVariantRepository productVariantRepository,
                            ProductDetailRepository productDetailRepository,
                            // CategoryService categoryService,
                            AttributeRepository attributeRepository) {
            this.productRepository = productRepository;
            this.categoryRepository = categoryRepository;
            this.shopRepository = shopRepository;
            this.slugify = Slugify.builder().build();
            this.cloudinary = cloudinary;
            this.productVariantRepository = productVariantRepository;
            this.productDetailRepository = productDetailRepository;
            // this.categoryService = categoryService;
            this.attributeRepository = attributeRepository;

      }

      @Transactional
      public ProductDTO createProduct(
              ProductCreateDTO request,
              MultipartFile defaultImageFile,
              List<MultipartFile> imagesList,
              MultipartFile defaultVideoIntroFile,
              List<ProductVariantCreateDTO> variants,
              Map<String, MultipartFile> imageVariants) {
            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Category not found with id: " + request.getCategoryId()));

            ShopEntity shop = shopRepository.findById(request.getShopId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Shop not found with id: " + request.getShopId()));

            String defaultImageUrl = null;
            if (defaultImageFile != null && !defaultImageFile.isEmpty()) {
                  try {
                        Map uploadResult = cloudinary.uploader().upload(defaultImageFile.getBytes(),
                                ObjectUtils.emptyMap());
                        defaultImageUrl = (String) uploadResult.get("secure_url");
                  } catch (Exception e) {
                        throw new RuntimeException("Failed to upload image", e);
                  }
            }

            // Upload video intro nếu có
            String defaultVideoIntroUrl = null;
            if (defaultVideoIntroFile != null && !defaultVideoIntroFile.isEmpty()) {
                  try {
                        Map uploadResult = cloudinary.uploader().upload(defaultVideoIntroFile.getBytes(),
                                ObjectUtils.asMap("resource_type", "video"));
                        defaultVideoIntroUrl = (String) uploadResult.get("secure_url");
                  } catch (Exception e) {
                        throw new RuntimeException("Failed to upload video", e);
                  }
            }

            ProductEntity product = new ProductEntity();
            product.setName(request.getName());
            product.setDefaultPrice(request.getDefaultPrice());
            product.setBrand(request.getBrand());
            product.setDescription(request.getDescription());
            product.setDefaultImage(defaultImageUrl);
            product.setDefaultVideoIntro(defaultVideoIntroUrl);
            product.setHidden(request.isHidden());
            product.setStatus(request.getStatus());
            product.setStock(request.getStock());
            product.setSold(request.getSold());
            product.setCategory(category);
            product.setShop(shop);
            product.setCreatedAt(LocalDateTime.now());

            // Generate and set slug
            String slug = slugify.slugify(request.getName() + "-" + System.currentTimeMillis());
            product.setSlug(slug);

            // Xử lý imagesList (upload lên Cloudinary, lưu vào bảng images)
            List<ImageEntity> imageEntities = new ArrayList<>();
            if (imagesList != null && !imagesList.isEmpty()) {
                  int priority = 1;
                  for (MultipartFile imageFile : imagesList) {
                        if (imageFile != null && !imageFile.isEmpty()) {
                              try {
                                    Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
                                            ObjectUtils.emptyMap());
                                    String imageUrl = (String) uploadResult.get("secure_url");
                                    ImageEntity image = new ImageEntity();
                                    image.setUrl(imageUrl);
                                    image.setPriority(priority++);
                                    image.setProduct(product);
                                    imageEntities.add(image);
                              } catch (Exception e) {
                                    throw new RuntimeException("Failed to upload image in imagesList", e);
                              }
                        }
                  }
            }
            product.setImagesList(imageEntities);

            ProductEntity savedProduct = productRepository.save(product);

            if (variants != null && !variants.isEmpty()) {
                  List<ProductVariantEntity> variantEntities = new ArrayList<>();
                  for (int i = 0; i < variants.size(); i++) {
                        ProductVariantCreateDTO variantReq = variants.get(i);
                        ProductVariantEntity variant = new ProductVariantEntity();
                        variant.setPrice(variantReq.getPrice());
                        variant.setStock(variantReq.getStock());
                        variant.setHidden(variantReq.isHidden());
                        variant.setProduct(savedProduct);
                        variant.setCreatedAt(LocalDateTime.now());
                        // Xử lý attributes
                        if (variantReq.getAttributes() != null) {
                              Set<AttributeEntity> attributes = new HashSet<>();
                              for (AttributeDTO attrDto : variantReq.getAttributes()) {
                                    // Tìm attribute có sẵn hoặc tạo mới
                                    AttributeEntity attr = attributeRepository
                                            .findByNameAndValue(attrDto.getName(), attrDto.getValue())
                                            .orElseGet(() -> {
                                                  AttributeEntity newAttr = new AttributeEntity();
                                                  newAttr.setName(attrDto.getName());
                                                  newAttr.setValue(attrDto.getValue());
                                                  return attributeRepository.save(newAttr);
                                            });
                                    attributes.add(attr);
                              }
                              variant.setAttributes(attributes);
                        }
                        // Xử lý imageVariant
                        String imageKey = variantReq.getImageVariant();
                        if (imageKey != null && imageVariants != null && imageVariants.containsKey(imageKey)) {
                              MultipartFile imageFile = imageVariants.get(imageKey);
                              if (imageFile != null && !imageFile.isEmpty()) {
                                    try {
                                          Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
                                                  ObjectUtils.emptyMap());
                                          String imageUrl = (String) uploadResult.get("secure_url");
                                          variant.setImageVariant(imageUrl);
                                    } catch (Exception e) {
                                          throw new RuntimeException("Failed to upload imageVariant for variant", e);
                                    }
                              }
                        }
                        variantEntities.add(variant);
                  }
                  if (!variantEntities.isEmpty()) {
                        System.out.println("Saving " + variantEntities.size() + " variants to database");
                        productVariantRepository.saveAll(variantEntities);
                        System.out.println("Variants saved successfully");
                  }
            }

            // Lấy lại product (cùng với variants nếu có !!!)
            ProductEntity productWithVariants = productRepository.findByIdWithVariants(savedProduct.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found after creation"));

            return toResponse(productWithVariants);
      }

      public ProductDTO getProductBySlug(String slug) {
            ProductEntity product = productRepository.findBySlug(slug)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with slug: " + slug));
            return toResponse(product);
      }

      public ProductDTO getProductById(UUID productId) {
            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
            return toResponse(product);
      }

      public List<ProductDTO> getAllProducts(boolean includeHidden) {
            List<ProductEntity> products;
            if (includeHidden) {
                  products = productRepository.findAll();
            } else {
                  products = productRepository.findByHiddenFalse();
            }
            return products.stream().map(this::toResponse).collect(java.util.stream.Collectors.toList());
      }

      @Transactional
      public ProductDTO updateProduct(UUID productId, ProductUpdateDTO request) {
            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

            // Update fields if provided
            if (request.getName() != null) {
                  product.setName(request.getName());
                  // Update slug when name changes
                  String slug = slugify.slugify(request.getName() + "-" + System.currentTimeMillis());
                  product.setSlug(slug);
            }
            if (request.getDefaultPrice() != null) {
                  product.setDefaultPrice(request.getDefaultPrice());
            }
            if (request.getBrand() != null) {
                  product.setBrand(request.getBrand());
            }
            if (request.getDescription() != null) {
                  product.setDescription(request.getDescription());
            }
            if (request.getCategoryId() != null) {
                  CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                          .orElseThrow(() -> new EntityNotFoundException(
                                  "Category not found with id: " + request.getCategoryId()));
                  product.setCategory(category);
            }
            if (request.getStock() != null && request.getStock() >= 0) {
                  product.setStock(request.getStock());
            }
            if (request.getHidden() != null) {
                  product.setHidden(request.getHidden());
            }

            product.setUpdatedAt(LocalDateTime.now());

            ProductEntity updatedProduct = productRepository.save(product);
            return toResponse(updatedProduct);
      }

      public ProductDetailEntity createProductDetail(UUID productId, ProductDetailEntity detail) {
            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));
            detail.setProduct(product);
            return productDetailRepository.save(detail);
      }

      public List<ProductDetailEntity> getProductDetailsByProductId(UUID productId) {
            return productDetailRepository.findByProduct_ProductId(productId);
      }

      private ProductDTO toResponse(ProductEntity product) {
            ProductDTO response = new ProductDTO();
            response.setProductId(product.getProductId());
            response.setName(product.getName());
            response.setDefaultPrice(product.getDefaultPrice());
            response.setBrand(product.getBrand());
            response.setDescription(product.getDescription());
            response.setDefaultImage(product.getDefaultImage());
            response.setDefaultVideoIntro(product.getDefaultVideoIntro());
            response.setHidden(product.isHidden());
            response.setStatus(product.getStatus());
            response.setSlug(product.getSlug());
            response.setStock(product.getStock());
            response.setSold(product.getSold());
            response.setCreatedAt(product.getCreatedAt());
            response.setUpdatedAt(product.getUpdatedAt());

            ProductDTO.CategoryInfo categoryInfo = new ProductDTO.CategoryInfo();
            categoryInfo.setId(product.getCategory().getId());
            categoryInfo.setName(product.getCategory().getName());
            response.setCategory(categoryInfo);

            ProductDTO.ShopInfo shopInfo = new ProductDTO.ShopInfo();
            shopInfo.setId(product.getShop().getId());
            shopInfo.setName(product.getShop().getName());
            response.setShop(shopInfo);

            // Bổ sung set imagesList
            if (product.getImagesList() != null && !product.getImagesList().isEmpty()) {
                  List<ImageDTO> imageDtos = product.getImagesList().stream().map(image -> {
                        ImageDTO dto = new ImageDTO();
                        dto.setImageId(image.getImageId());
                        dto.setUrl(image.getUrl());
                        dto.setPriority(image.getPriority());
                        return dto;
                  }).collect(java.util.stream.Collectors.toList());
                  response.setImagesList(imageDtos);
            } else {
                  response.setImagesList(new java.util.ArrayList<>());
            }

            // Bổ sung set variants
            if (product.getVariants() != null && !product.getVariants().isEmpty()) {
                  List<ProductVariantDTO> variantResponses = product.getVariants().stream().map(variant -> {
                        ProductVariantDTO variantResponse = new ProductVariantDTO();
                        variantResponse.setProductVariantId(variant.getProductVariantId());
                        variantResponse.setPrice(variant.getPrice());
                        variantResponse.setStock(variant.getStock());
                        variantResponse.setHidden(variant.isHidden());
                        variantResponse.setCreatedAt(variant.getCreatedAt());
                        variantResponse.setProductId(variant.getProduct().getProductId());
                        variantResponse.setImageVariant(variant.getImageVariant());

                        // Set attributes
                        if (variant.getAttributes() != null && !variant.getAttributes().isEmpty()) {
                              Set<AttributeDTO> attributeDtos = variant.getAttributes().stream().map(attr -> {
                                    AttributeDTO attrDto = new AttributeDTO();
                                    attrDto.setName(attr.getName());
                                    attrDto.setValue(attr.getValue());
                                    return attrDto;
                              }).collect(java.util.stream.Collectors.toSet());
                              variantResponse.setAttributes(attributeDtos);
                        }

                        return variantResponse;
                  }).collect(java.util.stream.Collectors.toList());
                  response.setVariants(variantResponses);
            } else {
                  response.setVariants(new java.util.ArrayList<>());
            }

            return response;
      }

}