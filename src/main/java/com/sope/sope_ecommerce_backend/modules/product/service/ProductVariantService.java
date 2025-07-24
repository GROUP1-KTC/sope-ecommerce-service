package com.sope.sope_ecommerce_backend.modules.product.service;

import com.sope.sope_ecommerce_backend.modules.product.dto.AttributeDto;
import com.sope.sope_ecommerce_backend.modules.product.dto.ImageDto;
import com.sope.sope_ecommerce_backend.modules.product.dto.ProductVariantCreateRequest;
import com.sope.sope_ecommerce_backend.modules.product.dto.ProductVariantResponse;
import com.sope.sope_ecommerce_backend.modules.product.entity.Attribute;
import com.sope.sope_ecommerce_backend.modules.product.entity.Image;
import com.sope.sope_ecommerce_backend.modules.product.entity.Product;
import com.sope.sope_ecommerce_backend.modules.product.entity.ProductVariant;
import com.sope.sope_ecommerce_backend.modules.product.repository.AttributeRepository;
import com.sope.sope_ecommerce_backend.modules.product.repository.ImageRepository;
import com.sope.sope_ecommerce_backend.modules.product.repository.ProductRepository;
import com.sope.sope_ecommerce_backend.modules.product.repository.ProductVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductVariantService {
      @Autowired
      private ProductRepository productRepository;
      @Autowired
      private ProductVariantRepository productVariantRepository;
      @Autowired
      private AttributeRepository attributeRepository;
      @Autowired
      private ImageRepository imageRepository;

      @Transactional
      public ProductVariantResponse createProductVariant(ProductVariantCreateRequest request) {
            Product product = productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            ProductVariant variant = new ProductVariant();
            variant.setPrice(request.getPrice());
            variant.setStock(request.getStock());
            variant.setHidden(request.isHidden());
            variant.setProduct(product);
            variant.setCreatedAt(LocalDateTime.now());

            // Xử lý attributes
            Set<Attribute> attributes = new HashSet<>();
            if (request.getAttributes() != null) {
                  for (AttributeDto attrDto : request.getAttributes()) {
                        Attribute attr = new Attribute();
                        attr.setName(attrDto.getName());
                        attr.setValue(attrDto.getValue());
                        if (attrDto.getImageId() != null) {
                              Image img = imageRepository.findById(attrDto.getImageId()).orElse(null);
                              attr.setImage(img);
                        }
                        // Lưu attribute trước khi add vào set
                        Attribute savedAttr = attributeRepository.save(attr);
                        attributes.add(savedAttr);
                  }
            }
            variant.setAttributes(attributes);

            // Xử lý images
            List<Image> images = new ArrayList<>();
            if (request.getImages() != null) {
                  for (ImageDto imgDto : request.getImages()) {
                        Image img = new Image();
                        img.setUrl(imgDto.getUrl());
                        img.setPriority(imgDto.getPriority());
                        img.setProductVariant(variant);
                        images.add(img);
                  }
            }
            variant.setImages(images);

            ProductVariant saved = productVariantRepository.save(variant);
            return toResponse(saved);
      }

      public ProductVariantResponse toResponse(ProductVariant variant) {
            ProductVariantResponse response = new ProductVariantResponse();
            response.setProductVariantId(variant.getProductVariantId());
            response.setPrice(variant.getPrice());
            response.setStock(variant.getStock());
            response.setHidden(variant.isHidden());
            response.setSlug(variant.getSlug());
            response.setCreatedAt(variant.getCreatedAt());
            response.setProductId(variant.getProduct().getProductId());

            // Map attributes
            if (variant.getAttributes() != null) {
                  Set<AttributeDto> attributeDtos = variant.getAttributes().stream().map(attr -> {
                        AttributeDto dto = new AttributeDto();
                        dto.setName(attr.getName());
                        dto.setValue(attr.getValue());
                        if (attr.getImage() != null) {
                              dto.setImageId(attr.getImage().getImageId());
                        }
                        return dto;
                  }).collect(Collectors.toSet());
                  response.setAttributes(attributeDtos);
            }

            // Map images
            if (variant.getImages() != null) {
                  List<ImageDto> imageDtos = variant.getImages().stream().map(img -> {
                        ImageDto dto = new ImageDto();
                        dto.setUrl(img.getUrl());
                        dto.setPriority(img.getPriority());
                        return dto;
                  }).collect(Collectors.toList());
                  response.setImages(imageDtos);
            }

            return response;
      }

      public ProductVariantResponse getProductVariantById(UUID variantId) {
            ProductVariant variant = productVariantRepository.findById(variantId)
                        .orElseThrow(() -> new EntityNotFoundException("ProductVariant not found"));
            return toResponse(variant);
      }
}
