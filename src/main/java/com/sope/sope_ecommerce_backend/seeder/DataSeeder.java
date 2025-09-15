package com.sope.sope_ecommerce_backend.seeder;

import com.sope.sope_ecommerce_backend.entities.*;
import com.sope.sope_ecommerce_backend.entities.OrderItem;
import com.sope.sope_ecommerce_backend.entities.OrderItemId;
import com.sope.sope_ecommerce_backend.enums.*;
import com.sope.sope_ecommerce_backend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository appUserRepository;
    private final AddressRepository addressRepository;
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public void run(String... args) throws Exception {

        seedRoles();
        seedData();

    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            Role admin = new Role(RoleName.ADMIN);
            Role customer = new Role(RoleName.USER);
            Role seller = new Role(RoleName.SELLER);

            roleRepository.saveAll(Arrays.asList(admin, customer, seller));
            System.out.println("✅ Roles seeded");
        } else {
            System.out.println("ℹ️ Roles already exist, skipping...");
        }
    }


    private void seedData() {
        if (appUserRepository.count() > 0) {
            System.out.println("ℹ️ Data already exists, skipping seeder...");
            return;
        }
        // 1. Users - Thêm 3 users nữa (tổng 5: 2 shop owners + 3 customers)
        AppUser user1 = AppUser.builder()
                .username("phuc")
                .password("123456")
                .name("Phạm Văn Phúc")
                .phone("0123456789")
                .email("phuc@example.com")
                .birthday(LocalDate.of(2000, 1, 1))
                .gender(Gender.MALE)
                .status(UserStatus.ACTIVE)
                .build();

        AppUser user2 = AppUser.builder()
                .username("hao")
                .password("123456")
                .name("Võ Nhật Hào")
                .phone("0987654321")
                .email("hao@example.com")
                .birthday(LocalDate.of(2000, 5, 5))
                .gender(Gender.MALE)
                .status(UserStatus.ACTIVE)
                .build();

        AppUser user3 = AppUser.builder()  // Customer 1
                .username("lan")
                .password("123456")
                .name("Nguyễn Thị Lan")
                .phone("0111222333")
                .email("lan@example.com")
                .birthday(LocalDate.of(1995, 3, 15))
                .gender(Gender.FEMALE)
                .status(UserStatus.ACTIVE)
                .build();

        AppUser user4 = AppUser.builder()  // Customer 2
                .username("minh")
                .password("123456")
                .name("Trần Văn Minh")
                .phone("0444555666")
                .email("minh@example.com")
                .birthday(LocalDate.of(1985, 7, 20))
                .gender(Gender.MALE)
                .status(UserStatus.ACTIVE)
                .build();

        AppUser user5 = AppUser.builder()  // Shop owner 3
                .username("shop3")
                .password("123456")
                .name("Lê Thị Hoa")
                .phone("0777888999")
                .email("hoa@example.com")
                .birthday(LocalDate.of(1990, 11, 10))
                .gender(Gender.FEMALE)
                .status(UserStatus.ACTIVE)
                .build();

        appUserRepository.saveAll(Arrays.asList(user1, user2, user3, user4, user5));

        // 2. Addresses - Thêm 3 addresses nữa (tổng 5)
        Address addr1 = Address.builder()
                .appUser(user1)
                .recipientName("Phuc")
                .phoneNumber("0123456789")
                .street("123 Đường A")
                .ward("Phường 1")
                .district("Quận 1")
                .city("Hồ Chí Minh")
                .country("Vietnam")
                .isDefault(true)
                .build();

        Address addr2 = Address.builder()
                .appUser(user2)
                .recipientName("Hao")
                .phoneNumber("0987654321")
                .street("456 Đường B")
                .ward("Phường 2")
                .district("Quận 3")
                .city("Hồ Chí Minh")
                .country("Vietnam")
                .isDefault(true)
                .build();

        Address addr3 = Address.builder()  // For user3
                .appUser(user3)
                .recipientName("Lan")
                .phoneNumber("0111222333")
                .street("789 Đường C")
                .ward("Phường 4")
                .district("Quận 7")
                .city("Hồ Chí Minh")
                .country("Vietnam")
                .isDefault(true)
                .build();

        Address addr4 = Address.builder()  // For user4
                .appUser(user4)
                .recipientName("Minh")
                .phoneNumber("0444555666")
                .street("101 Đường D")
                .ward("Phường 5")
                .district("Quận Bình Thạnh")
                .city("Hồ Chí Minh")
                .country("Vietnam")
                .isDefault(true)
                .build();

        Address addr5 = Address.builder()  // For user5
                .appUser(user5)
                .recipientName("Hoa")
                .phoneNumber("0777888999")
                .street("112 Đường E")
                .ward("Phường 6")
                .district("Quận 10")
                .city("Hồ Chí Minh")
                .country("Vietnam")
                .isDefault(true)
                .build();

        addressRepository.saveAll(Arrays.asList(addr1, addr2, addr3, addr4, addr5));

        // 3. Shops - Thêm 2 shops nữa (tổng 4)
        Shop shop1 = Shop.builder()
                .appUser(user1)
                .name("Shop Thời Trang Phúc")
                .email("shopphuc@example.com")
                .phone("0123456789")
                .status(Shop.Status.ACTIVE)
                .build();

        Shop shop2 = Shop.builder()
                .appUser(user2)
                .name("Shop Thời Trang Hào")
                .email("shophao@example.com")
                .phone("0987654321")
                .status(Shop.Status.ACTIVE)
                .build();

        Shop shop3 = Shop.builder()
                .appUser(user5)
                .name("Shop Phụ Kiện Hoa")
                .email("shophua@example.com")
                .phone("0777888999")
                .status(Shop.Status.ACTIVE)
                .build();

        Shop shop4 = Shop.builder()
                .appUser(user3)
                .name("Shop Giày Phúc")
                .email("shopgiayphuc@example.com")
                .phone("0123456789")
                .status(Shop.Status.ACTIVE)
                .build();

        shopRepository.saveAll(Arrays.asList(shop1, shop2, shop3, shop4));

        // 4. Categories - Thêm 3 categories nữa (tổng 5, với level và commission khác)
        Category cat1 = Category.builder()
                .name("Thời Trang Nam")
                .slug("thoi-trang-nam")
                .level(1)
                .commissionFeePercent(BigDecimal.valueOf(5))
                .build();

        Category cat2 = Category.builder()
                .name("Thời Trang Nữ")
                .slug("thoi-trang-nu")
                .level(1)
                .commissionFeePercent(BigDecimal.valueOf(5))
                .build();

        Category cat3 = Category.builder()  // Sub-category for testing hierarchy
                .name("Áo Nam")
                .slug("ao-nam")
                .level(2)
                .commissionFeePercent(BigDecimal.valueOf(3))
                .build();

        Category cat4 = Category.builder()
                .name("Phụ Kiện")
                .slug("phu-kien")
                .level(1)
                .commissionFeePercent(BigDecimal.valueOf(7))
                .build();

        Category cat5 = Category.builder()
                .name("Giày Dép")
                .slug("giay-dep")
                .level(1)
                .commissionFeePercent(BigDecimal.valueOf(4))
                .build();

        categoryRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5));

        // 5. Products - Thêm 6 products nữa (tổng 8)
        Product prod1 = Product.builder()
                .name("Áo Sơ Mi Nam")
                .brand("XYZ Fashion")
                .description("Áo sơ mi nam chất liệu cotton mềm mại, kiểu dáng trẻ trung")
                .defaultImage("default1.jpg")
                .status(StatusProduct.APPROVED)
                .category(cat1)
                .shop(shop1)
                .build();

        Product prod2 = Product.builder()
                .name("Váy Nữ")
                .brand("ABC Style")
                .description("Váy nữ dịu dàng, phù hợp mọi dịp")
                .defaultImage("default2.jpg")
                .status(StatusProduct.APPROVED)
                .category(cat2)
                .shop(shop2)
                .build();

        Product prod3 = Product.builder()
                .name("Quần Jeans Nam")
                .brand("Denim Pro")
                .description("Quần jeans nam bền bỉ, form slim fit")
                .defaultImage("default3.jpg")
                .status(StatusProduct.APPROVED)
                .category(cat3)  // Sub-category
                .shop(shop1)
                .build();

        Product prod4 = Product.builder()
                .name("Áo Khoác Nữ")
                .brand("Winter Wear")
                .description("Áo khoác nữ ấm áp cho mùa đông")
                .defaultImage("default4.jpg")
                .status(StatusProduct.APPROVED)
                .category(cat2)
                .shop(shop2)
                .build();

        Product prod5 = Product.builder()
                .name("Túi Xách")
                .brand("Bag Lux")
                .description("Túi xách thời trang cao cấp")
                .defaultImage("default5.jpg")
                .status(StatusProduct.APPROVED)
                .category(cat4)
                .shop(shop3)
                .build();

        Product prod6 = Product.builder()
                .name("Giày Thể Thao")
                .brand("Sport Shoe")
                .description("Giày thể thao nam thoải mái cho chạy bộ")
                .defaultImage("default6.jpg")
                .status(StatusProduct.APPROVED)
                .category(cat5)
                .shop(shop4)
                .build();

        Product prod7 = Product.builder()
                .name("Đồng Hồ")
                .brand("Watch Elite")
                .description("Đồng hồ nam cổ điển")
                .defaultImage("default7.jpg")
                .status(StatusProduct.APPROVED)
                .category(cat4)
                .shop(shop3)
                .build();

        Product prod8 = Product.builder()
                .name("Sandal Nữ")
                .brand("Summer Foot")
                .description("Sandal nữ nhẹ nhàng cho hè")
                .defaultImage("default8.jpg")
                .status(StatusProduct.APPROVED)
                .category(cat5)
                .shop(shop4)
                .build();

        productRepository.saveAll(Arrays.asList(prod1, prod2, prod3, prod4, prod5, prod6, prod7, prod8));

        // 6. ProductVariants - Thêm 12 variants nữa (tổng 14, với stock/sold đa dạng cho analytics)
        ProductVariant variant1 = ProductVariant.builder()
                .product(prod1)
                .price(BigDecimal.valueOf(200000))
                .stock(10)
                .sold(2)
                .build();

        ProductVariant variant2 = ProductVariant.builder()
                .product(prod2)
                .price(BigDecimal.valueOf(300000))
                .stock(5)
                .sold(1)
                .build();

        ProductVariant variant3 = ProductVariant.builder()
                .product(prod3)
                .price(BigDecimal.valueOf(250000))
                .stock(15)
                .sold(5)
                .build();

        ProductVariant variant4 = ProductVariant.builder()
                .product(prod4)
                .price(BigDecimal.valueOf(400000))
                .stock(8)
                .sold(3)
                .build();

        ProductVariant variant5 = ProductVariant.builder()
                .product(prod5)
                .price(BigDecimal.valueOf(500000))
                .stock(20)
                .sold(0)
                .build();

        ProductVariant variant6 = ProductVariant.builder()
                .product(prod6)
                .price(BigDecimal.valueOf(350000))
                .stock(12)
                .sold(4)
                .build();

        ProductVariant variant7 = ProductVariant.builder()
                .product(prod7)
                .price(BigDecimal.valueOf(600000))
                .stock(6)
                .sold(2)
                .build();

        ProductVariant variant8 = ProductVariant.builder()
                .product(prod8)
                .price(BigDecimal.valueOf(150000))
                .stock(25)
                .sold(10)  // High sold for testing hot items
                .build();

        // Additional variants for existing products (e.g., different sizes/colors)
        ProductVariant variant1_extra = ProductVariant.builder()
                .product(prod1)
                .price(BigDecimal.valueOf(220000))  // Slightly higher price
                .stock(5)
                .sold(1)
                .build();

        ProductVariant variant2_extra = ProductVariant.builder()
                .product(prod2)
                .price(BigDecimal.valueOf(320000))
                .stock(3)
                .sold(0)
                .build();

        ProductVariant variant3_extra = ProductVariant.builder()
                .product(prod3)
                .price(BigDecimal.valueOf(270000))
                .stock(10)
                .sold(3)
                .build();

        ProductVariant variant4_extra = ProductVariant.builder()
                .product(prod4)
                .price(BigDecimal.valueOf(420000))
                .stock(4)
                .sold(1)
                .build();

        ProductVariant variant5_extra = ProductVariant.builder()
                .product(prod5)
                .price(BigDecimal.valueOf(550000))
                .stock(15)
                .sold(2)
                .build();

        ProductVariant variant6_extra = ProductVariant.builder()
                .product(prod6)
                .price(BigDecimal.valueOf(370000))
                .stock(7)
                .sold(0)
                .build();

        productVariantRepository.saveAll(Arrays.asList(
                variant1, variant2, variant3, variant4, variant5, variant6, variant7, variant8,
                variant1_extra, variant2_extra, variant3_extra, variant4_extra, variant5_extra, variant6_extra
        ));

        // 7. Orders - Thêm 5 orders nữa (tổng 6, với status đa dạng cho analytics)
        Order order1 = Order.builder()
                .appUser(user2)
                .shop(shop1)
                .shippingAddress(addr2)
                .status(OrderStatus.PENDING)
                .subTotal(BigDecimal.valueOf(200000))
                .totalAmount(BigDecimal.valueOf(200000))
                .shippingRateId("SHIP123")
                .idempotencyKey(UUID.randomUUID().toString())
                .orderNumber("ORDER001")
                .build();

        Order order2 = Order.builder()
                .appUser(user3)
                .shop(shop2)
                .shippingAddress(addr3)
                .status(OrderStatus.CONFIRMED)
                .subTotal(BigDecimal.valueOf(700000))
                .totalAmount(BigDecimal.valueOf(720000))  // + shipping
                .shippingRateId("SHIP456")
                .idempotencyKey(UUID.randomUUID().toString())
                .orderNumber("ORDER002")
                .build();

        Order order3 = Order.builder()
                .appUser(user4)
                .shop(shop1)
                .shippingAddress(addr4)
                .status(OrderStatus.DELIVERED)
                .subTotal(BigDecimal.valueOf(450000))
                .totalAmount(BigDecimal.valueOf(450000))
                .shippingRateId("SHIP789")
                .idempotencyKey(UUID.randomUUID().toString())
                .orderNumber("ORDER003")
                .build();

        Order order4 = Order.builder()
                .appUser(user3)
                .shop(shop3)
                .shippingAddress(addr3)
                .status(OrderStatus.DELIVERED)
                .subTotal(BigDecimal.valueOf(650000))
                .totalAmount(BigDecimal.valueOf(670000))
                .shippingRateId("SHIP101")
                .idempotencyKey(UUID.randomUUID().toString())
                .orderNumber("ORDER004")
                .build();

        Order order5 = Order.builder()
                .appUser(user4)
                .shop(shop4)
                .shippingAddress(addr4)
                .status(OrderStatus.CANCELLED)
                .subTotal(BigDecimal.valueOf(150000))
                .totalAmount(BigDecimal.valueOf(150000))
                .shippingRateId("SHIP112")
                .idempotencyKey(UUID.randomUUID().toString())
                .orderNumber("ORDER005")
                .build();

        Order order6 = Order.builder()
                .appUser(user2)
                .shop(shop2)
                .shippingAddress(addr2)
                .status(OrderStatus.PENDING)
                .subTotal(BigDecimal.valueOf(950000))
                .totalAmount(BigDecimal.valueOf(970000))
                .shippingRateId("SHIP131")
                .idempotencyKey(UUID.randomUUID().toString())
                .orderNumber("ORDER006")
                .build();

        orderRepository.saveAll(Arrays.asList(order1, order2, order3, order4, order5, order6));

        // 8. OrderItems - Thêm nhiều items (tổng 10, mỗi order có 1-3 items)
        // Order1: 1 item
        OrderItemId orderItemId1_1 = new OrderItemId();
        orderItemId1_1.setOrderId(order1.getOrderId());
        orderItemId1_1.setProductVariantId(variant1.getProductVariantId());

        OrderItem orderItem1_1 = OrderItem.builder()
                .orderItemId(orderItemId1_1)
                .order(order1)
                .productVariant(variant1)
                .quantity(1)
                .price(variant1.getPrice())
                .build();

        // Order2: 2 items
        OrderItemId orderItemId2_1 = new OrderItemId();
        orderItemId2_1.setOrderId(order2.getOrderId());
        orderItemId2_1.setProductVariantId(variant2.getProductVariantId());

        OrderItem orderItem2_1 = OrderItem.builder()
                .orderItemId(orderItemId2_1)
                .order(order2)
                .productVariant(variant2)
                .quantity(1)
                .price(variant2.getPrice())
                .build();

        OrderItemId orderItemId2_2 = new OrderItemId();
        orderItemId2_2.setOrderId(order2.getOrderId());
        orderItemId2_2.setProductVariantId(variant4.getProductVariantId());

        OrderItem orderItem2_2 = OrderItem.builder()
                .orderItemId(orderItemId2_2)
                .order(order2)
                .productVariant(variant4)
                .quantity(1)
                .price(variant4.getPrice())
                .build();

        // Order3: 1 item
        OrderItemId orderItemId3_1 = new OrderItemId();
        orderItemId3_1.setOrderId(order3.getOrderId());
        orderItemId3_1.setProductVariantId(variant3.getProductVariantId());

        OrderItem orderItem3_1 = OrderItem.builder()
                .orderItemId(orderItemId3_1)
                .order(order3)
                .productVariant(variant3)
                .quantity(2)  // Quantity >1 for testing
                .price(variant3.getPrice())
                .build();

        // Order4: 3 items
        OrderItemId orderItemId4_1 = new OrderItemId();
        orderItemId4_1.setOrderId(order4.getOrderId());
        orderItemId4_1.setProductVariantId(variant5.getProductVariantId());

        OrderItem orderItem4_1 = OrderItem.builder()
                .orderItemId(orderItemId4_1)
                .order(order4)
                .productVariant(variant5)
                .quantity(1)
                .price(variant5.getPrice())
                .build();

        OrderItemId orderItemId4_2 = new OrderItemId();
        orderItemId4_2.setOrderId(order4.getOrderId());
        orderItemId4_2.setProductVariantId(variant7.getProductVariantId());

        OrderItem orderItem4_2 = OrderItem.builder()
                .orderItemId(orderItemId4_2)
                .order(order4)
                .productVariant(variant7)
                .quantity(1)
                .price(variant7.getPrice())
                .build();

        OrderItemId orderItemId4_3 = new OrderItemId();
        orderItemId4_3.setOrderId(order4.getOrderId());
        orderItemId4_3.setProductVariantId(variant1_extra.getProductVariantId());

        OrderItem orderItem4_3 = OrderItem.builder()
                .orderItemId(orderItemId4_3)
                .order(order4)
                .productVariant(variant1_extra)
                .quantity(1)
                .price(variant1_extra.getPrice())
                .build();

        // Order6: 2 items (skip order5 as CANCELLED for testing)
        OrderItemId orderItemId6_1 = new OrderItemId();
        orderItemId6_1.setOrderId(order6.getOrderId());
        orderItemId6_1.setProductVariantId(variant2_extra.getProductVariantId());

        OrderItem orderItem6_1 = OrderItem.builder()
                .orderItemId(orderItemId6_1)
                .order(order6)
                .productVariant(variant2_extra)
                .quantity(2)
                .price(variant2_extra.getPrice())
                .build();

        OrderItemId orderItemId6_2 = new OrderItemId();
        orderItemId6_2.setOrderId(order6.getOrderId());
        orderItemId6_2.setProductVariantId(variant6.getProductVariantId());

        OrderItem orderItem6_2 = OrderItem.builder()
                .orderItemId(orderItemId6_2)
                .order(order6)
                .productVariant(variant6)
                .quantity(1)
                .price(variant6.getPrice())
                .build();

        orderItemRepository.saveAll(Arrays.asList(
                orderItem1_1, orderItem2_1, orderItem2_2, orderItem3_1,
                orderItem4_1, orderItem4_2, orderItem4_3, orderItem6_1, orderItem6_2
        ));

        // Gắn OrderItems vào Orders (cho các orders có items)
        order1.setOrderItems(Arrays.asList(orderItem1_1));
        order2.setOrderItems(Arrays.asList(orderItem2_1, orderItem2_2));
        order3.setOrderItems(Arrays.asList(orderItem3_1));
        order4.setOrderItems(Arrays.asList(orderItem4_1, orderItem4_2, orderItem4_3));
        order6.setOrderItems(Arrays.asList(orderItem6_1, orderItem6_2));
        orderRepository.saveAll(Arrays.asList(order1, order2, order3, order4, order6));

        System.out.println("✅ Seeder data created successfully with expanded data for algorithms!");
    }
}