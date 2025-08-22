package com.sope.sope_ecommerce_backend.services;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import com.sope.sope_ecommerce_backend.dto.request.ProductCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.ProductUpdateDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductBasicWithVariantsDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductDTO;
import com.sope.sope_ecommerce_backend.dto.response.ProductVariantDetailDTO;

public interface ProductService {
      ProductDTO getProductBySlug(String slug);

      List<ProductDTO> getAllProducts();

      ProductBasicWithVariantsDTO getProductWithVariants(UUID productId);

      ProductVariantDetailDTO getProductVariantDetail(UUID productVariantId);

      ProductDTO createProduct(ProductCreateDTO dto,
                  MultipartFile defaultImage,
                  MultipartFile defaultVideoIntro,
                  List<MultipartFile> productImages,
                  List<MultipartFile> variantImages);

      ProductDTO updateProduct(String slug,
                  ProductUpdateDTO dto,
                  MultipartFile defaultVideoIntro,
                  List<MultipartFile> productImages,
                  List<MultipartFile> variantImages);
}
