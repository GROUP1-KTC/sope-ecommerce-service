package com.sope.sope_ecommerce_backend.services;

import java.util.List;
import java.util.UUID;

import com.sope.sope_ecommerce_backend.dto.response.*;
import com.sope.sope_ecommerce_backend.entities.Product;
import com.sope.sope_ecommerce_backend.enums.StatusProduct;

import org.springframework.cglib.core.internal.Function;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import com.sope.sope_ecommerce_backend.dto.request.ProductCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ProductUpdateDTO;

public interface ProductService {
      ProductDTO getProductBySlug(String slug);

      List<ProductDTO> getAllProducts();


      Page<ProductDTO> getProductsByShop(int page, int size);

      Page<ProductDTO> getApprovedProducts(int page, int size);

      ProductDTO updateProductStatus(UUID productId, StatusProduct status);

      Page<ProductSummaryResponse> getApprovedProductsByShop(UUID shopId, int page, int size);

      Page<ProductSummaryResponse> getProductsByCategoryIncludingChildren(@PathVariable String slug, int page,
                  int size);

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

      List<ProductSummaryResponse>  searchProductsByKeywords(List<String> keywords, int limit);

      List<ProductSummaryResponse> searchProductsByImage(MultipartFile image, int limit);

      ProductDTO getProductById(UUID productId);

      List<ProductSummaryResponse> getProductsByName(String name, int limit);

}
