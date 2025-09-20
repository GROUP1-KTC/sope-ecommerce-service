package com.sope.sope_ecommerce_backend.seeder.orders;

import com.sope.sope_ecommerce_backend.entities.*;
import com.sope.sope_ecommerce_backend.enums.OrderStatus;
import com.sope.sope_ecommerce_backend.enums.StatusProduct;
import com.sope.sope_ecommerce_backend.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
@AllArgsConstructor
public class OrderSeeder {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public void run() throws Exception {
        // Xóa dữ liệu cũ (chỉ dùng trong môi trường phát triển)
        orderRepository.deleteAll();
        productVariantRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        shopRepository.deleteAll();

        // Seed dữ liệu mẫu cho shops, categories, products và product_variants
        seedSampleShopsCategoriesAndProducts();

        // Seed orders cho người dùng
        createOrderForUser("user1", 2); // 2 orders cho user1
        createOrderForUser("user2", 1); // 1 order cho user2
        createOrderForUser("user3", 1); // 1 order cho user3
        createOrderForUser("user4", 1); // 1 order cho user4
        createOrderForUser("admin", 1); // 1 order cho admin
    }

    private void seedSampleShopsCategoriesAndProducts() {
        // Seed shops
        if (shopRepository.count() == 0) {
            Shop shop1 = Shop.builder()
                    .name("Shop Thời Trang")
                    .email("shopthoitrang@example.com")
                    .phone("0123456789")
                    .status(Shop.Status.ACTIVE) // nếu là boolean
                    .isMall(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            Shop shop2 = Shop.builder()
                    .name("Shop Giày Dép")
                    .email("shopgiaydep@example.com")
                    .phone("0987654321")
                    .status(Shop.Status.ACTIVE)
                    .isMall(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            shopRepository.saveAll(Arrays.asList(shop1, shop2));
            System.out.println("✅ Đã tạo 2 cửa hàng mẫu.");
        }

        // Seed categories
        if (categoryRepository.count() == 0) {
            Category category1 = Category.builder().name("Thời Trang Nam").build();
            Category category2 = Category.builder().name("Thời Trang Nữ").build();
            Category category3 = Category.builder().name("Giày Dép").build();
            categoryRepository.saveAll(Arrays.asList(category1, category2, category3));
            System.out.println("✅ Đã tạo 3 danh mục mẫu.");
        }

        // Seed products and product variants
        if (productRepository.count() == 0) {
            Shop shop1 = shopRepository.findByName("Shop Thời Trang")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy shop"));
            Shop shop2 = shopRepository.findByName("Shop Giày Dép")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy shop"));
            Category category1 = categoryRepository.findByName("Thời Trang Nam")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
            Category category2 = categoryRepository.findByName("Thời Trang Nữ")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
            Category category3 = categoryRepository.findByName("Giày Dép")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

            List<Product> products = Arrays.asList(
                    Product.builder()
                            .name("Áo Thun")
                            .shop(shop1)
                            .category(category1)
                            .defaultImage("ao-thun.jpg")
                            .status(StatusProduct.APPROVED)
                            .slug("ao-thun")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build(),
                    Product.builder()
                            .name("Quần Jean")
                            .shop(shop1)
                            .category(category1)
                            .defaultImage("quan-jean.jpg")
                            .status(StatusProduct.APPROVED)
                            .slug("quan-jean")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build(),
                    Product.builder()
                            .name("Váy Nữ")
                            .shop(shop1)
                            .category(category2)
                            .defaultImage("vay-nu.jpg")
                            .status(StatusProduct.APPROVED)
                            .slug("vay-nu")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build(),
                    Product.builder()
                            .name("Giày Thể Thao")
                            .shop(shop2)
                            .category(category3)
                            .defaultImage("giay-the-thao.jpg")
                            .status(StatusProduct.APPROVED)
                            .slug("giay-the-thao")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build(),
                    Product.builder()
                            .name("Dép Sandal")
                            .shop(shop2)
                            .category(category3)
                            .defaultImage("dep-sandal.jpg")
                            .status(StatusProduct.APPROVED)
                            .slug("dep-sandal")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build(),
                    Product.builder()
                            .name("Mũ Lưỡi Trai")
                            .shop(shop2)
                            .category(category3)
                            .defaultImage("mu-luoi-trai.jpg")
                            .status(StatusProduct.APPROVED)
                            .slug("mu-luoi-trai")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build()
            );
            productRepository.saveAll(products);

            // Seed product variants
            List<ProductVariant> variants = new ArrayList<>();
            for (Product product : products) {
                variants.add(ProductVariant.builder()
                        .product(product)
                        .price(new BigDecimal("150000"))
                        .stock(100)
                        .sold(0)
                        .imageVariant(product.getDefaultImage())
                        .build());
                variants.add(ProductVariant.builder()
                        .product(product)
                        .price(new BigDecimal("200000"))
                        .stock(80)
                        .sold(0)
                        .imageVariant(product.getDefaultImage())
                        .build());
            }
            productVariantRepository.saveAll(variants);
            System.out.println("✅ Đã tạo 6 sản phẩm và 12 biến thể sản phẩm mẫu.");
        }
    }

    private void createOrderForUser(String username, int orderCount) {
        // Tìm người dùng
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));

        // Tìm địa chỉ mặc định
        Address defaultAddress = addressRepository.findByAppUserAndIsDefault(user, true)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ mặc định cho người dùng: " + username));

        // Tìm shop
        Shop shop = shopRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Không tìm thấy shop"));

        // Tạo orders
        for (int i = 0; i < orderCount; i++) {
            String idempotencyKey = UUID.randomUUID().toString();
            if (orderRepository.findByIdempotencyKey(idempotencyKey).isEmpty()) {
                String orderNumber = "ORD-" + LocalDateTime.now().getYear() + "-" + String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));

                // Tạo order mà không có orderItems trước
                Order order = Order.builder()
                        .appUser(user)
                        .shop(shop)
                        .shippingAddress(defaultAddress)
                        .orderDate(LocalDateTime.now())
                        .expireAt(LocalDateTime.now().plusDays(7))
                        .subTotal(BigDecimal.ZERO)
                        .shippingCharges(new BigDecimal("15000"))
                        .totalAmount(BigDecimal.ZERO)
                        .note("Giao hàng nhanh")
                        .status(OrderStatus.PENDING)
                        .orderItems(new ArrayList<>())
                        .idempotencyKey(idempotencyKey)
                        .orderNumber(orderNumber)
                        .trackingNumber("TRK-" + UUID.randomUUID().toString().substring(0, 8))
                        .shippingRateId("SHR-" + UUID.randomUUID().toString().substring(0, 8))
                        .discounts(new HashSet<>())
                        .statusHistory(new ArrayList<>())
                        .build();

                // Lưu order để có orderId
                orderRepository.save(order);

                // Thêm 5-6 order items
                List<ProductVariant> productVariants = productVariantRepository.findAll();
                if (productVariants.size() < 6) {
                    throw new RuntimeException("Không đủ product variants để tạo order");
                }
                Collections.shuffle(productVariants);
                int itemCount = ThreadLocalRandom.current().nextInt(5, 7); // 5 hoặc 6 sản phẩm
                BigDecimal subTotal = BigDecimal.ZERO;

                for (int j = 0; j < itemCount; j++) {
                    ProductVariant variant = productVariants.get(j);
                    int quantity = ThreadLocalRandom.current().nextInt(1, 4);
                    BigDecimal price = variant.getPrice();
                    BigDecimal itemTotal = price.multiply(new BigDecimal(quantity));
                    subTotal = subTotal.add(itemTotal);

                    OrderItem orderItem = OrderItem.builder()
                            .orderItemId(new OrderItemId(order.getOrderId(), variant.getProductVariantId()))
                            .order(order)
                            .productVariant(variant)
                            .quantity(quantity)
                            .price(price)
                            .build();

                    order.getOrderItems().add(orderItem);
                }

                // Cập nhật tổng tiền
                order.setSubTotal(subTotal);
                order.setTotalAmount(subTotal.add(order.getShippingCharges()));

                // Lưu lại order với orderItems
                orderRepository.save(order);
                System.out.println("✅ Đã tạo đơn hàng: " + orderNumber + " cho người dùng: " + username + " với " + itemCount + " sản phẩm");
            }
        }
    }
}