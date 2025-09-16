package com.sope.sope_ecommerce_backend.services;

import java.util.List;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.dto.response.*;
import com.sope.sope_ecommerce_backend.entities.Product;
import org.springframework.cglib.core.internal.Function;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import com.sope.sope_ecommerce_backend.dto.request.ProductCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ProductUpdateDTO;

public interface ProductService {
      ProductDTO getProductBySlug(String slug);

      Page<ProductDTO> getProductsByShop(UUID shopId, int page, int size);

      List<ProductByCategory> getProductsByCategoryIncludingChildren(@PathVariable String slug);

      ProductBasicWithVariantsDTO getProductWithVariants(UUID productId);

      ProductVariantDetailDTO getProductVariantDetail(UUID productVariantId);

      ProductDTO createProduct(ProductCreateDTO dto,
                  MultipartFile defaultImage,
                  MultipartFile defaultVideoIntro,
                  List<MultipartFile> productImages,
                  List<MultipartFile> variantImages);

      ProductDTO updateProduct(String slug,
                  ProductUpdateDTO dto,
                  MultipartFile defaultImage,
                  MultipartFile defaultVideoIntro,
                  List<MultipartFile> productImages,
                  List<MultipartFile> variantImages);

      List<ProductSummaryResponse> getInitProducts();

      List<ProductSummaryResponse> getInitProductsForGuest();

      List<ProductSummaryResponse> getSimilarProducts(UUID productId, int limit);

      List<ProductSummaryResponse> getSuggestedProducts(UUID productId, int limit);

      List<ProductSummaryResponse> getProducts(UUID productId, int limit, Function<Product, List<Product>> relatedFunc);
}
