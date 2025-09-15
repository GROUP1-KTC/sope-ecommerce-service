package com.sope.sope_ecommerce_backend.seeder.product;

import com.sope.sope_ecommerce_backend.entities.Category;
import com.sope.sope_ecommerce_backend.entities.Product;
import com.sope.sope_ecommerce_backend.entities.ProductVariant;
import com.sope.sope_ecommerce_backend.entities.Shop;
import com.sope.sope_ecommerce_backend.enums.StatusProduct;
import com.sope.sope_ecommerce_backend.repositories.CategoryRepository;
import com.sope.sope_ecommerce_backend.repositories.ProductRepository;
import com.sope.sope_ecommerce_backend.repositories.ShopRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ProductSeeder {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;

    public void run() {
        // Tìm các cửa hàng theo tên
        Optional<Shop> shopA = shopRepository.findByName("Shop Nguyễn Văn A");
        Optional<Shop> shopB = shopRepository.findByName("Shop Trần Thị B");
        Optional<Shop> shopC = shopRepository.findByName("Shop Lê Văn C");
        Optional<Shop> shopD = shopRepository.findByName("Shop Phạm Thị D");

        if (shopA.isEmpty() || shopB.isEmpty() || shopC.isEmpty() || shopD.isEmpty()) {
            throw new RuntimeException("Không tìm thấy một hoặc nhiều cửa hàng để tạo sản phẩm!");
        }

        // Tìm các danh mục từ CategorySeeder
        Optional<Category> jacketCategory = categoryRepository.findByName("Áo khoác");
        Optional<Category> aoThunCategory = categoryRepository.findByName("Áo thun");
        Optional<Category> quanNuCategory = categoryRepository.findByName("Quần");
        Optional<Category> winterJacketCategory = categoryRepository.findByName("Áo khoác mùa đông");
        Optional<Category> kidFashionCategory = categoryRepository.findByName("Thời trang trẻ em");

        if (jacketCategory.isEmpty() || aoThunCategory.isEmpty() || quanNuCategory.isEmpty() ||
                winterJacketCategory.isEmpty() || kidFashionCategory.isEmpty()) {
            throw new RuntimeException("Không tìm thấy danh mục cần thiết!");
        }

        // Tạo sản phẩm cho Shop Nguyễn Văn A
        createProduct(
                "Áo khoác nam mùa đông cao cấp",
                "Áo khoác nam giữ ấm, phong cách hiện đại",
                "brandX",
                "https://example.com/images/jacket.jpg",
                StatusProduct.APPROVED,
                winterJacketCategory.get(),
                shopA.get(),
                List.of(
                        createProductVariant(new BigDecimal("500000"), 50, 10, "https://example.com/images/jacket_black.jpg"),
                        createProductVariant(new BigDecimal("550000"), 30, 5, "https://example.com/images/jacket_blue.jpg")
                )
        );

        // Tạo sản phẩm cho Shop Trần Thị B
        createProduct(
                "Áo thun nam cotton",
                "Áo thun nam chất liệu cotton thoáng mát",
                "brandY",
                "https://example.com/images/tshirt.jpg",
                StatusProduct.APPROVED,
                aoThunCategory.get(),
                shopB.get(),
                List.of(
                        createProductVariant(new BigDecimal("150000"), 100, 20, "https://example.com/images/tshirt_white.jpg"),
                        createProductVariant(new BigDecimal("160000"), 80, 15, "https://example.com/images/tshirt_black.jpg")
                )
        );

        // Tạo sản phẩm cho Shop Lê Văn C
        createProduct(
                "Quần jeans nữ cao cấp",
                "Quần jeans nữ thời trang, form dáng chuẩn",
                "brandZ",
                "https://example.com/images/jeans.jpg",
                StatusProduct.APPROVED,
                quanNuCategory.get(),
                shopC.get(),
                List.of(
                        createProductVariant(new BigDecimal("350000"), 60, 8, "https://example.com/images/jeans_blue.jpg"),
                        createProductVariant(new BigDecimal("370000"), 40, 12, "https://example.com/images/jeans_black.jpg")
                )
        );

        // Tạo sản phẩm cho Shop Phạm Thị D
        createProduct(
                "Áo thun trẻ em dễ thương",
                "Áo thun trẻ em chất liệu cotton mềm mại",
                "brandK",
                "https://example.com/images/kid_tshirt.jpg",
                StatusProduct.APPROVED,
                kidFashionCategory.get(),
                shopD.get(),
                List.of(
                        createProductVariant(new BigDecimal("120000"), 70, 15, "https://example.com/images/kid_tshirt_red.jpg"),
                        createProductVariant(new BigDecimal("130000"), 50, 10, "https://example.com/images/kid_tshirt_blue.jpg")
                )
        );
    }

    private Product createProduct(String name, String description, String brand, String defaultImage,
                                  StatusProduct status, Category category, Shop shop, List<ProductVariant> variants) {
        Optional<Product> existing = productRepository.findByName(name);
        if (existing.isPresent()) {
            return existing.get();
        }

        // Tạo sản phẩm mới
        Product product = Product.builder()
                .name(name)
                .description(description)
                .brand(brand)
                .defaultImage(defaultImage)
                .status(status)
                .category(category)
                .shop(shop)
                .variants(variants)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .hidden(false)
                .build();

        // Tạo slug
        String slug = generateSlug(name, product, category);
        product.setSlug(slug);

        // Gán sản phẩm cho các biến thể
        for (ProductVariant variant : variants) {
            variant.setProduct(product);
        }

        return productRepository.save(product);
    }

    private ProductVariant createProductVariant(BigDecimal price, int stock, int sold, String imageVariant) {
        return ProductVariant.builder()
                .price(price)
                .stock(stock)
                .sold(sold)
                .imageVariant(imageVariant)
                .build();
    }

    private String generateSlug(String name, Product product, Category category) {
        String nameSlug = toSlug(name);
        String productIdShort = product.getProductId() != null
                ? product.getProductId().toString().substring(0, 8)
                : UUID.randomUUID().toString().substring(0, 8);

        String categoryIdShort = category.getId().toString().substring(0, 8);
        return nameSlug + "-" + categoryIdShort + "." + productIdShort;
    }

    private String toSlug(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return normalized.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}