package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.ProductCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ProductUpdateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ProductVariantRequestDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductBasicWithVariantsDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductVariantDetailDTO;
import com.sope.sope_ecommerce_backend.entities.*;
import com.sope.sope_ecommerce_backend.entities.Attribute;
import com.sope.sope_ecommerce_backend.enums.StatusProduct;
import com.sope.sope_ecommerce_backend.exception.ResourceNotFoundException;
import com.sope.sope_ecommerce_backend.mapper.ProductMapper;
import com.sope.sope_ecommerce_backend.mapper.ProductVariantMapper;
import com.sope.sope_ecommerce_backend.repositories.*;
import com.sope.sope_ecommerce_backend.services.ProductService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.slugify.Slugify;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
      private final ProductRepository productRepository;
      private final ProductVariantRepository productVariantRepository;
      private final ProductMapper productMapper;
      private final ProductVariantMapper productVariantMapper;
      // private final AttributeMapper attributeMapper;
      private final CategoryRepository categoryRepository;
      private final ShopRepository shopRepository;
      private final Cloudinary cloudinary;
      private final AttributeRepository attributeRepository;

      @Override
      @Transactional(readOnly = true)
      public ProductDTO getProductBySlug(String slug) {
            return productRepository.findBySlug(slug)
                        .map(productMapper::toDto)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with slug: " + slug));
      }

      @Override
      @Transactional(readOnly = true)
      public List<ProductDTO> getAllProducts() {
            List<Product> products = productRepository.findAll();
            return productMapper.toDtoList(products);
      }

      @Override
      @Transactional(readOnly = true)
      public ProductBasicWithVariantsDTO getProductWithVariants(UUID productId) {
            Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

            List<Map<String, String>> variantObjects = product.getVariants().stream()
                        .map(variant -> variant.getAttributes().stream()
                                    .collect(Collectors.toMap(
                                                attr -> attr.getName(),
                                                attr -> attr.getValue())))
                        .toList();

            return ProductBasicWithVariantsDTO.builder()
                        .productId(product.getProductId())
                        .name(product.getName())
                        .variants(variantObjects)
                        .build();
      }

      @Override
      @Transactional(readOnly = true)
      public ProductVariantDetailDTO getProductVariantDetail(UUID productVariantId) {
            ProductVariant variant = productVariantRepository.findById(productVariantId)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                    "ProductVariant not found with id: " + productVariantId));
            return productVariantMapper.toProductVariantDetailDTO(variant);
      }

      @Override
      @Transactional
      public ProductDTO createProduct(ProductCreateDTO dto,
                  MultipartFile defaultImage,
                  MultipartFile defaultVideoIntro,
                  List<MultipartFile> productImages,
                  List<MultipartFile> variantFiles) {
            Product entity = productMapper.toEntity(dto);

            // category and shop
            Category category = categoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            Shop shop = shopRepository.findById(dto.getShopId())
                        .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));
            entity.setCategory(category);
            entity.setShop(shop);

            entity.setDefaultImage(uploadFileToCloudinary(defaultImage, "image"));
            entity.setDefaultVideoIntro(
                        defaultVideoIntro != null ? uploadFileToCloudinary(defaultVideoIntro, "video") : null);
            entity.setStatus(StatusProduct.PENDING);
            entity.setCreatedAt(LocalDateTime.now());

            // product images
            if (productImages != null && !productImages.isEmpty()) {
                  AtomicInteger priority = new AtomicInteger(1);
                  entity.setImagesList(productImages.stream().map(file -> {
                        String url = uploadFileToCloudinary(file, "image");
                        ImageEntity imageEntity = new ImageEntity();
                        imageEntity.setUrl(url);
                        imageEntity.setPriority(priority.getAndIncrement());
                        imageEntity.setProduct(entity);
                        return imageEntity;
                  }).collect(Collectors.toList()));
            }

            // Nếu không có variants -> tạo variant mặc định
            if (entity.getVariants() == null || entity.getVariants().isEmpty()) {
                  ProductVariant defaultVariant = new ProductVariant();
                  defaultVariant.setPrice(entity.getDefaultPrice());
                  defaultVariant.setStock(entity.getStock());
                  defaultVariant.setSold(0);
                  defaultVariant.setImageVariant(entity.getDefaultImage()); // URL ảnh mặc định
                  defaultVariant.setProduct(entity);
                  defaultVariant.setAttributes(new HashSet<>());

                  entity.setVariants(new ArrayList<>(List.of(defaultVariant)));
            }

            // Xử lý attributes cho từng variant
            for (ProductVariant variant : entity.getVariants()) {
                  Set<Attribute> managedAttributes = new HashSet<>();
                  if (variant.getAttributes() != null) {
                        for (Attribute attr : variant.getAttributes()) {
                              Optional<Attribute> existingAttr = attributeRepository
                                          .findByNameAndValue(attr.getName(), attr.getValue());
                              managedAttributes.add(existingAttr.orElseGet(() -> attributeRepository.save(attr)));
                        }
                  }
                  variant.setAttributes(managedAttributes);
                  variant.setProduct(entity);
            }

            // Xử lý ảnh cho variants
            Map<String, String> uploadedVariantUrls = new HashMap<>();
            Map<String, MultipartFile> variantFileMap = (variantFiles != null)
                        ? variantFiles.stream().collect(Collectors.toMap(
                                    f -> normalizeFileName(f.getOriginalFilename()),
                                    f -> f,
                                    (a, b) -> a))
                        : Collections.emptyMap();

            for (ProductVariant variant : entity.getVariants()) {
                  if (variant.getImageVariant() != null && variant.getImageVariant().startsWith("http")) {
                        continue;
                  }

                  String fileName = normalizeFileName(variant.getImageVariant());
                  if (fileName == null || fileName.isEmpty()) {
                        variant.setImageVariant(null);
                        continue;
                  }

                  if (uploadedVariantUrls.containsKey(fileName)) {
                        variant.setImageVariant(uploadedVariantUrls.get(fileName));
                        continue;
                  }

                  MultipartFile variantFile = variantFileMap.get(fileName);
                  if (variantFile != null) {
                        String url = uploadFileToCloudinary(variantFile, "image");
                        uploadedVariantUrls.put(fileName, url);
                        variant.setImageVariant(url);
                  } else {
                        System.out.println("Warning: Variant image file not found for: " + fileName);
                        variant.setImageVariant(null);
                  }
            }

            productRepository.save(entity);

            String first8ProductId = entity.getProductId().toString().substring(0, 8);
            String first8ShopId = shop.getId().toString().substring(0, 8);

            Slugify slugify = Slugify.builder().build();
            entity.setSlug(slugify.slugify(dto.getName()) + "-" + first8ProductId + "-" + first8ShopId);

            Product savedEntity = productRepository.save(entity);

            return productMapper.toDto(savedEntity);
      }

      @Override
      @Transactional
      public ProductDTO updateProduct(
                  String slug,
                  ProductUpdateDTO dto,
                  MultipartFile defaultVideoIntro,
                  List<MultipartFile> productImages,
                  List<MultipartFile> variantFiles) {

            Product entity = productRepository.findBySlug(slug)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with slug: " + slug));

            if (dto.getDefaultPrice() != null) {
                  entity.setDefaultPrice(dto.getDefaultPrice());
            }
            if (dto.getStock() != null) {
                  entity.setStock(dto.getStock());
            }
            if (dto.getHidden() != null) {
                  entity.setHidden(dto.getHidden());
            }
            if (dto.getDescription() != null) {
                  entity.setDescription(dto.getDescription());
            }

            productMapper.updateEntityFromDto(dto, entity);

            if (defaultVideoIntro != null && !defaultVideoIntro.isEmpty()) {
                  entity.setDefaultVideoIntro(uploadFileToCloudinary(defaultVideoIntro, "video"));
            }

            if (dto.getImageUrlsToKeep() != null || productImages != null) {
                  List<ImageEntity> currentImages = entity.getImagesList();
                  if (currentImages == null) {
                        currentImages = new ArrayList<>();
                        entity.setImagesList(currentImages);
                  }

                  // 1. Xóa ảnh nào không còn trong danh sách giữ lại
                  if (dto.getImageUrlsToKeep() != null) {
                        currentImages.removeIf(img -> !dto.getImageUrlsToKeep().contains(img.getUrl()));
                  }

                  // 2. Thêm ảnh mới
                  if (productImages != null && !productImages.isEmpty()) {
                        int maxPriority = currentImages.stream()
                                    .mapToInt(ImageEntity::getPriority)
                                    .max()
                                    .orElse(0);

                        AtomicInteger priority = new AtomicInteger(maxPriority + 1);

                        for (MultipartFile file : productImages) {
                              String url = uploadFileToCloudinary(file, "image");
                              ImageEntity imageEntity = new ImageEntity();
                              imageEntity.setUrl(url);
                              imageEntity.setPriority(priority.getAndIncrement());
                              imageEntity.setProduct(entity);
                              currentImages.add(imageEntity);
                        }
                  }
            }

            if (dto.getVariants() != null) {
                  Map<UUID, ProductVariant> existingVariants = entity.getVariants().stream()
                              .collect(Collectors.toMap(ProductVariant::getProductVariantId, v -> v));

                  for (ProductVariantRequestDTO variantDTO : dto.getVariants()) {
                        ProductVariant variantEntity;
                        if (variantDTO.getProductVariantId() != null
                                    && existingVariants.containsKey(variantDTO.getProductVariantId())) {
                              // --- Variant cũ ---
                              variantEntity = existingVariants.get(variantDTO.getProductVariantId());
                              updateVariantFromDTO(variantEntity, variantDTO);
                        } else {
                              // --- Variant mới ---
                              variantEntity = new ProductVariant();
                              variantEntity.setProduct(entity);
                              updateVariantFromDTO(variantEntity, variantDTO);
                              entity.getVariants().add(variantEntity);
                        }
                  }
            }

            if (variantFiles != null && !variantFiles.isEmpty() && entity.getVariants() != null) {
                  Map<String, MultipartFile> variantFileMap = variantFiles.stream()
                              .collect(Collectors.toMap(
                                          f -> normalizeFileName(f.getOriginalFilename()),
                                          f -> f,
                                          (a, b) -> a));

                  Map<String, String> uploadedUrls = new HashMap<>();

                  for (ProductVariant variant : entity.getVariants()) {
                        String fileName = normalizeFileName(variant.getImageVariant());
                        if (fileName != null && variantFileMap.containsKey(fileName)) {
                              if (!uploadedUrls.containsKey(fileName)) {
                                    String url = uploadFileToCloudinary(variantFileMap.get(fileName), "image");
                                    uploadedUrls.put(fileName, url);
                              }
                              variant.setImageVariant(uploadedUrls.get(fileName));
                        }
                  }
            }

            entity.setUpdatedAt(LocalDateTime.now());

            productRepository.save(entity);

            return productMapper.toDto(entity);
      }

      private void updateVariantFromDTO(ProductVariant entity, ProductVariantRequestDTO dto) {
            if (dto.getPrice() != null)
                  entity.setPrice(dto.getPrice());
            if (dto.getStock() != null)
                  entity.setStock(dto.getStock());

            if (dto.getAttributes() != null && !dto.getAttributes().isEmpty()) {
                  Set<Attribute> updatedAttributes = dto.getAttributes().stream()
                              .map(attrDTO -> getOrCreateAttribute(attrDTO.getName(), attrDTO.getValue()))
                              .collect(Collectors.toSet());
                  entity.setAttributes(updatedAttributes);
            }
            if (dto.getImageVariant() != null)
                  entity.setImageVariant(dto.getImageVariant());
      }

      private Attribute getOrCreateAttribute(String name, String value) {
            return attributeRepository.findByNameAndValue(name, value)
                        .orElseGet(() -> {
                              Attribute newAttr = new Attribute();
                              newAttr.setName(name);
                              newAttr.setValue(value);
                              return attributeRepository.save(newAttr);
                        });
      }

      private String uploadFileToCloudinary(MultipartFile file, String type) {
            if (file != null && !file.isEmpty()) {
                  try {
                        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                                    "video".equals(type) ? ObjectUtils.asMap("resource_type", "video")
                                                : ObjectUtils.emptyMap());
                        return (String) uploadResult.get("secure_url");
                  } catch (Exception e) {
                        throw new RuntimeException("Failed to upload " + type, e);
                  }
            }
            return null;
      }

      private String normalizeFileName(String name) {
            return name == null ? null : name.trim().toLowerCase();
      }

}