package com.sope.sope_ecommerce_backend.services.impl;

import com.sope.sope_ecommerce_backend.dto.request.ProductCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ProductUpdateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ProductVariantRequestDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductBasicWithVariantsDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductByCategory;
import com.sope.sope_ecommerce_backend.dto.response.ProductDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductDetailDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductVariantByCategory;
import com.sope.sope_ecommerce_backend.dto.response.ProductVariantDetailDTO;
import com.sope.sope_ecommerce_backend.entities.*;
import com.sope.sope_ecommerce_backend.enums.StatusProduct;
import com.sope.sope_ecommerce_backend.mapper.ProductMapper;
import com.sope.sope_ecommerce_backend.mapper.ProductVariantMapper;
import com.sope.sope_ecommerce_backend.repositories.*;
import com.sope.sope_ecommerce_backend.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
                        .orElseThrow(() -> new EntityNotFoundException("Product not found with slug: " + slug));
      }

      @Override
      @Transactional(readOnly = true)
      public Page<ProductDTO> getProductsByShop(UUID shopId, int page, int size) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<Product> products = productRepository.findByShopId(shopId, pageable);
            return products.map(productMapper::toDto);
      }

      @Override
      @Transactional(readOnly = true)
      public List<ProductByCategory> getProductsByCategoryIncludingChildren(String slug) {
            Category category = categoryRepository.findBySlug(slug)
                        .orElseThrow(() -> new EntityNotFoundException("Category not found with slug: " + slug));

            Set<UUID> categoryIds = getAllChildCategoryIds(category.getId());

            categoryIds.add(category.getId());

            List<Product> products = productRepository.findByCategoryIdIn(new ArrayList<>(categoryIds));

            return products.stream()
                        .map(product -> new ProductByCategory(
                                    product.getProductId(),
                                    product.getName(),
                                    product.getSlug(),
                                    product.getBrand(),
                                    product.getDefaultImage(),
                                    product.getVariants().stream()
                                                .map(v -> new ProductVariantByCategory(v.getPrice(), v.getSold()))
                                                .toList()))
                        .toList();
      }

      private Set<UUID> getAllChildCategoryIds(UUID parentId) {
            Set<UUID> ids = new HashSet<>();
            List<Category> children = categoryRepository.findByParentId(parentId);
            for (Category child : children) {
                  ids.add(child.getId());
                  ids.addAll(getAllChildCategoryIds(child.getId())); // đệ quy
            }
            return ids;
      }

      @Cacheable(value = "products", key = "'allProducts'", unless = "#result == null || #result.isEmpty()")
      public List<ProductDTO> getAllProducts() {
            List<Product> products = productRepository.findAll();
            return productMapper.toDtoList(products);
      }

      @Override
      @Transactional(readOnly = true)
      public ProductBasicWithVariantsDTO getProductWithVariants(UUID productId) {
            Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));

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
                        .orElseThrow(() -> new EntityNotFoundException(
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
            Category category = categoryRepository.findById(dto.categoryId())
                        .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            Shop shop = shopRepository.findById(dto.shopId())
                        .orElseThrow(() -> new EntityNotFoundException("Shop not found"));
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

            for (ProductVariant variant : entity.getVariants()) {
                  List<Attribute> managedAttributes = new ArrayList<>();
                  if (variant.getAttributes() != null) {
                        Set<String> seen = new HashSet<>();
                        for (Attribute attr : variant.getAttributes()) {
                              String key = attr.getName() + ":" + attr.getValue();
                              if (seen.contains(key))
                                    continue;
                              seen.add(key);
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

            if (dto.productDetails() != null && !dto.productDetails().isEmpty()) {
                  List<ProductDetailEntity> details = dto.productDetails().stream()
                              .map(detailDTO -> {
                                    ProductDetailEntity detail = new ProductDetailEntity();
                                    detail.setLabel(detailDTO.label());
                                    detail.setData(detailDTO.data());
                                    detail.setPriority(detailDTO.priority());
                                    detail.setProduct(entity);
                                    return detail;
                              })
                              .collect(Collectors.toList());
                  entity.setProductDetails(details);
            }
            productRepository.save(entity);

            String first8ProductId = entity.getProductId().toString().substring(0, 8);
            String first8ShopId = shop.getId().toString().substring(0, 8);

            Slugify slugify = Slugify.builder().build();
            entity.setSlug(slugify.slugify(dto.name()) + "-" + first8ProductId + "-" + first8ShopId);

            Product savedEntity = productRepository.save(entity);

            return productMapper.toDto(savedEntity);
      }

      @Override
      @Transactional
      public ProductDTO updateProduct(
                  String slug,
                  ProductUpdateDTO dto,
                  MultipartFile defaultImage,
                  MultipartFile defaultVideoIntro,
                  List<MultipartFile> productImages,
                  List<MultipartFile> variantFiles) {

            Product entity = productRepository.findBySlug(slug)
                        .orElseThrow(() -> new EntityNotFoundException("Product not found with slug: " + slug));

            if (dto.hidden() != null) {
                  entity.setHidden(dto.hidden());
            }
            if (dto.description() != null) {
                  entity.setDescription(dto.description());
            }

            if (dto.productDetails() != null) {
                  Map<UUID, ProductDetailEntity> existingDetails = entity.getProductDetails() == null
                              ? new HashMap<>()
                              : entity.getProductDetails().stream()
                                          .filter(d -> d.getProductDetailId() != null)
                                          .collect(Collectors.toMap(ProductDetailEntity::getProductDetailId, d -> d));

                  List<ProductDetailEntity> updatedDetails = new ArrayList<>();

                  for (ProductDetailDTO detailDTO : dto.productDetails()) {
                        ProductDetailEntity detail;
                        if (detailDTO.productDetailId() != null
                                    && existingDetails.containsKey(detailDTO.productDetailId())) {
                              // update detail cũ
                              detail = existingDetails.get(detailDTO.productDetailId());
                        } else {
                              // thêm mới
                              detail = new ProductDetailEntity();
                              detail.setProduct(entity);
                        }
                        detail.setLabel(detailDTO.label());
                        detail.setData(detailDTO.data());
                        detail.setPriority(detailDTO.priority());
                        updatedDetails.add(detail);
                  }

                  entity.setProductDetails(updatedDetails);
            }

            productMapper.updateEntityFromDto(dto, entity);

            if (defaultImage != null && !defaultImage.isEmpty()) {
                  entity.setDefaultImage(uploadFileToCloudinary(defaultImage, "image"));
            }

            if (defaultVideoIntro != null && !defaultVideoIntro.isEmpty()) {
                  entity.setDefaultVideoIntro(uploadFileToCloudinary(defaultVideoIntro, "video"));
            }

            if (dto.imageUrlsToKeep() != null || productImages != null) {
                  List<ImageEntity> currentImages = entity.getImagesList();
                  if (currentImages == null) {
                        currentImages = new ArrayList<>();
                        entity.setImagesList(currentImages);
                  }
                  if (dto.imageUrlsToKeep() != null) {
                        currentImages.removeIf(img -> !dto.imageUrlsToKeep().contains(img.getUrl()));
                  }

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

            if (dto.variants() != null && !dto.variants().isEmpty()) {
                  List<ProductVariant> variants = entity.getVariants();
                  if (variants == null) {
                        variants = new ArrayList<>();
                        entity.setVariants(variants);
                  }

                  // Map theo id để update variant cũ
                  Map<UUID, ProductVariant> existingVariants = variants.stream()
                              .filter(v -> v.getProductVariantId() != null)
                              .collect(Collectors.toMap(ProductVariant::getProductVariantId, v -> v));

                  // Map theo attributeKey để tránh duplicate
                  Map<String, ProductVariant> attributeKeyToVariant = new LinkedHashMap<>();
                  for (ProductVariant v : variants) {
                        String key = v.getAttributes().stream()
                                    .map(a -> a.getName() + ":" + a.getValue())
                                    .collect(Collectors.joining("|"));
                        attributeKeyToVariant.putIfAbsent(key, v);
                  }

                  for (ProductVariantRequestDTO variantDTO : dto.variants()) {
                        ProductVariant variantEntity = null;

                        if (variantDTO.productVariantId() != null
                                    && existingVariants.containsKey(variantDTO.productVariantId())) {
                              variantEntity = existingVariants.get(variantDTO.productVariantId());
                        } else {
                              String newKey = variantDTO.attributes().stream()
                                          .map(a -> a.name() + ":" + a.value())
                                          .collect(Collectors.joining("|"));

                              if (attributeKeyToVariant.containsKey(newKey)) {
                                    variantEntity = attributeKeyToVariant.get(newKey);
                              } else {
                                    variantEntity = new ProductVariant();
                                    variantEntity.setProduct(entity);

                                    if (variantDTO.attributes().size() > 1) {
                                          String primaryAttrName = variantDTO.attributes().get(0).name();
                                          String primaryAttrValue = variantDTO.attributes().get(0).value();
                                          variants.stream()
                                                      .filter(v -> v.getImageVariant() != null)
                                                      .filter(v -> v.getAttributes().stream()
                                                                  .anyMatch(a -> a.getName()
                                                                              .equalsIgnoreCase(primaryAttrName)
                                                                              && a.getValue().equalsIgnoreCase(
                                                                                          primaryAttrValue)))
                                                      .map(ProductVariant::getImageVariant)
                                                      .findFirst()
                                                      .ifPresent(variantEntity::setImageVariant);
                                    }

                                    variants.add(variantEntity);
                                    attributeKeyToVariant.put(newKey, variantEntity);
                              }
                        }
                        updateVariantFromDTO(variantEntity, variantDTO);
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
                        String imagePath = variant.getImageVariant();
                        if (imagePath != null && variantFileMap.containsKey(imagePath)) {
                              if (!uploadedUrls.containsKey(imagePath)) {
                                    String url = uploadFileToCloudinary(variantFileMap.get(imagePath), "image");
                                    uploadedUrls.put(imagePath, url);
                              }
                              variant.setImageVariant(uploadedUrls.get(imagePath));
                        }
                  }
            }

            entity.setUpdatedAt(LocalDateTime.now());
            productRepository.save(entity);

            return productMapper.toDto(entity);
      }

      private void updateVariantFromDTO(ProductVariant entity, ProductVariantRequestDTO dto) {
            if (dto.price() != null) {
                  entity.setPrice(new BigDecimal(dto.price().toString()));
            }
            if (dto.stock() != null) {
                  entity.setStock(dto.stock());
            }
            if (dto.weight() != null) {
                  entity.setWeight(dto.weight());
            } else if (entity.getWeight() == null) {
                  entity.setWeight(BigDecimal.ZERO);
            }
            if (dto.dimension() != null) {
                  entity.setDimension(dto.dimension());
            } else if (entity.getDimension() == null) {
                  entity.setDimension(new Dimension(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));
            }
            if (dto.attributes() != null) {
                  List<Attribute> updatedAttributes = dto.attributes().stream()
                              .map(attrDTO -> getOrCreateAttribute(attrDTO.name(), attrDTO.value()))
                              .collect(Collectors.collectingAndThen(
                                          Collectors.toMap(
                                                      a -> a.getName() + ":" + a.getValue(),
                                                      a -> a,
                                                      (a, b) -> a,
                                                      LinkedHashMap::new),

                                          m -> new ArrayList<>(m.values())));
                  entity.setAttributes(updatedAttributes);
            }
            if (dto.imageVariant() != null) {
                  String iv = dto.imageVariant();
                  if (iv.startsWith("http://") || iv.startsWith("https://")) {
                        entity.setImageVariant(iv);
                  } else {
                        entity.setImageVariant(normalizeFileName(iv));
                  }
            }
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