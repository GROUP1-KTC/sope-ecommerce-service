package com.sope.sope_ecommerce_backend.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.sope.sope_ecommerce_backend.dto.response.ProductSummaryResponse;
import com.sope.sope_ecommerce_backend.entities.ReviewEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.sope.sope_ecommerce_backend.dto.request.ProductCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ProductUpdateDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductDTO;
import com.sope.sope_ecommerce_backend.entities.Product;

@Mapper(componentModel = "spring", uses = { ProductVariantMapper.class, ImageMapper.class, MultipartFileMapper.class })
public interface ProductMapper {

        ProductDTO toDto(Product product); // GET

        List<ProductDTO> toDtoList(List<Product> productEntities);

        // POST PUT
        @Mapping(target = "productId", ignore = true)
        @Mapping(target = "slug", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "category", ignore = true)
        @Mapping(target = "shop", ignore = true)
        @Mapping(target = "wishlists", ignore = true)
        @Mapping(target = "imagesList", ignore = true)
        @Mapping(target = "defaultImage", ignore = true)
        @Mapping(target = "defaultVideoIntro", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "variants", source = "variants")
        @Mapping(target = "productDetails", ignore = true)
        Product toEntity(ProductCreateDTO productCreateDTO); // POST

        @Mapping(target = "shop", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "wishlists", ignore = true)
        @Mapping(target = "productDetails", ignore = true)
        @Mapping(target = "imagesList", ignore = true)
        @Mapping(target = "defaultImage", ignore = true)
        @Mapping(target = "defaultVideoIntro", ignore = true)
        @Mapping(target = "slug", ignore = true)
        @Mapping(target = "productId", ignore = true)
        @Mapping(target = "updatedAt", ignore = true)
        @Mapping(target = "category", ignore = true)
        @Mapping(target = "name", ignore = true)
        @Mapping(target = "brand", ignore = true)
        @Mapping(target = "variants", ignore = true)
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void updateEntityFromDto(ProductUpdateDTO productUpdateDTO, @MappingTarget Product entity);

        default ProductSummaryResponse toProductSummaryResponse(Product product) {
                BigDecimal minPrice = product.getVariants().stream()
                                .map(v -> v.getPrice())
                                .min(BigDecimal::compareTo)
                                .orElse(BigDecimal.ZERO);

                int totalStock = product.getVariants().stream()
                                .mapToInt(v -> v.getStock())
                                .sum();

                int totalSold = product.getVariants().stream()
                                .mapToInt(v -> v.getSold())
                                .sum();

                List<ReviewEntity> allReviews = product.getVariants().stream()
                                .flatMap(variant -> variant.getReviews().stream())
                                .toList();

                Double averageRating = allReviews.isEmpty() ? null
                                : allReviews.stream()
                                                .mapToInt(ReviewEntity::getRating)
                                                .average()
                                                .orElse(0.0);

                return new ProductSummaryResponse(
                                product.getProductId(),
                                product.getName(),
                                product.getSlug(),
                                minPrice,
                                product.getDefaultImage(),
                                totalStock,
                                totalSold,
                                averageRating);
        }

}
