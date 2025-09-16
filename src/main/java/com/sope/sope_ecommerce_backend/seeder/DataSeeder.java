
// package com.sope.sope_ecommerce_backend.seeder;

// import com.sope.sope_ecommerce_backend.entities.*;
// import com.sope.sope_ecommerce_backend.enums.*;
// import com.sope.sope_ecommerce_backend.repositories.*;
// import lombok.RequiredArgsConstructor;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.stereotype.Component;

// import java.math.BigDecimal;
// import java.time.LocalDateTime;
// import java.util.*;

// @Component
// @RequiredArgsConstructor
// public class DataSeeder implements CommandLineRunner {

// private final UserRepository appUserRepository;
// private final AddressRepository addressRepository;
// private final ShopRepository shopRepository;
// private final CategoryRepository categoryRepository;
// private final ProductRepository productRepository;
// private final ProductVariantRepository productVariantRepository;
// private final OrderRepository orderRepository;
// private final OrderItemRepository orderItemRepository;
// private final RoleRepository roleRepository;
// private final BCryptPasswordEncoder passwordEncoder;

// @Override
// public void run(String... args) throws Exception {
// // 1. Roles

// createRoleIfNotExists("ADMIN");
// createRoleIfNotExists("USER");
// createRoleIfNotExists("SELLER");
// createRoleIfNotExists("SHIPPER");

// // 2. Users

// AppUser appUser1 = createUserIfNotExists("admin", "admin",
// "admin@example.com", "123456", List.of("ADMIN"),
// "https://res.cloudinary.com/dybo8zd4y123/image/upload/v1756968807/iilendxyjpj99vq7u9ro.jpg");

// AppUser appUser2 = createUserIfNotExists("shop", "shop", "shop@example.com",
// "123456",
// List.of("SELLER", "USER"),
// "https://res.cloudinary.com/dybo8zd4y123/image/upload/v1756889312/cylfn98scm57at8njpfp.jpg");

// AppUser appUser3 = createUserIfNotExists("lan", "Nguyễn Thị Lan",
// "lan@example.com", "123456", List.of("USER"),
// "https://res.cloudinary.com/dybo8zd4y123/image/upload/v1756889312/cylfn98scm57at8njpfp.jpg");

// AppUser appUser4 = createUserIfNotExists("minh", "Trần Văn Minh",
// "minh@example.com", "123456",
// List.of("USER"),
// "https://res.cloudinary.com/dybo8zd4y123/image/upload/v1756889312/cylfn98scm57at8njpfp.jpg");

// AppUser appUser5 = createUserIfNotExists("shop3", "Lê Thị Hoa",
// "hoa@example.com", "123456",
// List.of("SELLER", "USER"),
// "https://res.cloudinary.com/dybo8zd4y123/image/upload/v1757387506/afdaozkgbplsazyafovi.jpg");

// AppUser appUser6 = createUserIfNotExists("shipper", "shipper",
// "shipper@example.com", "123456",
// List.of("SHIPPER"),
// "https://res.cloudinary.com/dybo8zd4y123/image/upload/v1757321353/imdgjdubqnrl6w3tycj6.jpg");

// // 3. Shops - Thêm 2 shops nữa (tổng 4)

// Shop shop1 = createShopIfNotExists(
// "shop", "Shop bán đồ điện tử", "035782233", "shop@example.com", "987654321",
// "456 Another St, City", "Shop chuyên bán đồ điện tử",
// "https://res.cloudinary.com/dybo8zd4y123/image/upload/v1757908319/jhd1srfhohzrzudc7vnc.gif",
// false);

// Shop shop3 = createShopIfNotExists(
// "shop3", "Shop bán quần áo", "055782233", "shop3@example.com", "980654321",
// "456 ", "Shop chuyên bán đồ điện tử",
// "https://res.cloudinary.com/dybo8zd4y123/image/upload/v1757907798/dzg1fg4eaayh0p8hosq3.jpg",
// false);

// // 4. Categories - Thêm 3 categories nữa (tổng 5, với level và commission
// khác)
// Category cat1 = Category.builder()
// .name("Thời Trang Nam")
// .slug("thoi-trang-nam")
// .level(1)
// .commissionFeePercent(BigDecimal.valueOf(5))
// .build();

// Category cat2 = Category.builder()
// .name("Thời Trang Nữ")
// .slug("thoi-trang-nu")
// .level(1)
// .commissionFeePercent(BigDecimal.valueOf(5))
// .build();

// Category cat3 = Category.builder() // Sub-category for testing hierarchy
// .name("Áo Nam")
// .slug("ao-nam")
// .level(2)
// .commissionFeePercent(BigDecimal.valueOf(3))
// .build();

// Category cat4 = Category.builder()
// .name("Phụ Kiện")
// .slug("phu-kien")
// .level(1)
// .commissionFeePercent(BigDecimal.valueOf(7))
// .build();

// Category cat5 = Category.builder()
// .name("Giày Dép")
// .slug("giay-dep")
// .level(1)
// .commissionFeePercent(BigDecimal.valueOf(4))
// .build();

// categoryRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5));

// // 5. Products - Thêm 6 products nữa (tổng 8)
// Product prod1 = Product.builder()
// .name("Áo Sơ Mi Nam")
// .brand("XYZ Fashion")
// .slug("ao-so-mi-nam")
// .description("Áo sơ mi nam chất liệu cotton mềm mại, kiểu dáng trẻ trung")
// .defaultImage("https://res.cloudinary.com/dybo8zd4y123/image/upload/v1757906811/ezkafi83wakk1cxwc3fi.webp")
// .status(StatusProduct.APPROVED)
// .category(cat1)
// .shop(shop1)
// .build();

// Product prod2 = Product.builder()
// .name("Váy Nữ")
// .slug("vay-nu")
// .brand("ABC Style")
// .description("Váy nữ dịu dàng, phù hợp mọi dịp")
// .defaultImage("https://res.cloudinary.com/dybo8zd4y123/image/upload/v1757898729/x4o516yrk7uqvbnickty.jpg")
// .status(StatusProduct.APPROVED)
// .category(cat2)
// .shop(shop1)
// .build();

// Product prod3 = Product.builder()
// .name("Quần Jeans Nam")
// .slug("quan-jeans-nam")
// .brand("Denim Pro")
// .description("Quần jeans nam bền bỉ, form slim fit")
// .defaultImage("https://res.cloudinary.com/dybo8zd4y123/image/upload/v1757832968/zmpruokxtsnl6jdjicdk.jpg")
// .status(StatusProduct.APPROVED)
// .category(cat3) // Sub-category
// .shop(shop1)
// .build();

// Product prod4 = Product.builder()
// .name("Áo Khoác Nữ")
// .slug("ao-khoac-nu")
// .brand("Winter Wear")
// .description("Áo khoác nữ ấm áp cho mùa đông")
// .defaultImage("https://res.cloudinary.com/dybo8zd4y123/image/upload/v1757832931/kpfs0raizp2wtawb6xwn.jpg")
// .status(StatusProduct.APPROVED)
// .category(cat2)
// .shop(shop3)
// .build();

// Product prod5 = Product.builder()
// .name("Túi Xách")
// .slug("tui-xach")
// .brand("Bag Lux")
// .description("Túi xách thời trang cao cấp")
// .defaultImage("https://res.cloudinary.com/dybo8zd4y123/image/upload/v1757832757/nvdlwvwgarrlrtyfkaok.jpg")
// .status(StatusProduct.APPROVED)
// .category(cat4)
// .shop(shop3)
// .build();

// Product prod6 = Product.builder()
// .name("Giày Thể Thao")
// .slug("giay-the-thao")
// .brand("Sport Shoe")
// .description("Giày thể thao nam thoải mái cho chạy bộ")
// .defaultImage("https://res.cloudinary.com/dybo8zd4y123/image/upload/v1757788925/ipdgm8pjy0zlxgsg4i6z.jpg")
// .status(StatusProduct.APPROVED)
// .category(cat5)
// .shop(shop3)
// .build();

// Product prod7 = Product.builder()
// .name("Đồng Hồ")
// .slug("dong-ho")
// .brand("Watch Elite")
// .description("Đồng hồ nam cổ điển")
// .defaultImage("https://res.cloudinary.com/dybo8zd4y123/image/upload/v1757576070/e7jswpuu8qqhelgesvoc.png")
// .status(StatusProduct.APPROVED)
// .category(cat4)
// .shop(shop3)
// .build();

// Product prod8 = Product.builder()
// .name("Sandal Nữ")
// .slug("sandal-nu")
// .brand("Summer Foot")
// .description("Sandal nữ nhẹ nhàng cho hè")
// .defaultImage("https://res.cloudinary.com/dybo8zd4y123/image/upload/v1757526063/v1gepqksfjyfstf2svql.webp")
// .status(StatusProduct.APPROVED)
// .category(cat5)
// .shop(shop1)
// .build();

// productRepository.saveAll(Arrays.asList(prod1, prod2, prod3, prod4, prod5,
// prod6, prod7, prod8));

// // 6. ProductVariants - Thêm 12 variants nữa (tổng 14, với stock/sold đa dạng
// // cho analytics)
// ProductVariant variant1 = ProductVariant.builder()
// .product(prod1)
// .price(BigDecimal.valueOf(200000))
// .stock(10)
// .sold(2)
// .build();

// ProductVariant variant2 = ProductVariant.builder()
// .product(prod2)
// .price(BigDecimal.valueOf(300000))
// .stock(5)
// .sold(1)
// .build();

// ProductVariant variant3 = ProductVariant.builder()
// .product(prod3)
// .price(BigDecimal.valueOf(250000))
// .stock(15)
// .sold(5)
// .build();

// ProductVariant variant4 = ProductVariant.builder()
// .product(prod4)
// .price(BigDecimal.valueOf(400000))
// .stock(8)
// .sold(3)
// .build();

// ProductVariant variant5 = ProductVariant.builder()
// .product(prod5)
// .price(BigDecimal.valueOf(500000))
// .stock(20)
// .sold(0)
// .build();

// ProductVariant variant6 = ProductVariant.builder()
// .product(prod6)
// .price(BigDecimal.valueOf(350000))
// .stock(12)
// .sold(4)
// .build();

// ProductVariant variant7 = ProductVariant.builder()
// .product(prod7)
// .price(BigDecimal.valueOf(600000))
// .stock(6)
// .sold(2)
// .build();

// ProductVariant variant8 = ProductVariant.builder()
// .product(prod8)
// .price(BigDecimal.valueOf(150000))
// .stock(25)
// .sold(10) // High sold for testing hot items
// .build();

// // Additional variants for existing products (e.g., different sizes/colors)
// ProductVariant variant1_extra = ProductVariant.builder()
// .product(prod1)
// .price(BigDecimal.valueOf(220000)) // Slightly higher price
// .stock(5)
// .sold(1)
// .build();

// ProductVariant variant2_extra = ProductVariant.builder()
// .product(prod2)
// .price(BigDecimal.valueOf(320000))
// .stock(3)
// .sold(0)
// .build();

// ProductVariant variant3_extra = ProductVariant.builder()
// .product(prod3)
// .price(BigDecimal.valueOf(270000))
// .stock(10)
// .sold(3)
// .build();

// ProductVariant variant4_extra = ProductVariant.builder()
// .product(prod4)
// .price(BigDecimal.valueOf(420000))
// .stock(4)
// .sold(1)
// .build();

// ProductVariant variant5_extra = ProductVariant.builder()
// .product(prod5)
// .price(BigDecimal.valueOf(550000))
// .stock(15)
// .sold(2)
// .build();

// ProductVariant variant6_extra = ProductVariant.builder()
// .product(prod6)
// .price(BigDecimal.valueOf(370000))
// .stock(7)
// .sold(0)
// .build();

// productVariantRepository.saveAll(Arrays.asList(
// variant1, variant2, variant3, variant4, variant5, variant6, variant7,
// variant8,
// variant1_extra, variant2_extra, variant3_extra, variant4_extra,
// variant5_extra, variant6_extra));

// // 7. Orders - Thêm 5 orders nữa (tổng 6, với status đa dạng cho analytics)
// Order order1 = Order.builder()
// .appUser(appUser3)
// .shop(shop1)
// .shippingAddress(appUser3.getAddresses().get(0))
// .status(OrderStatus.PENDING)
// .subTotal(BigDecimal.valueOf(200000))
// .totalAmount(BigDecimal.valueOf(200000))
// .shippingRateId("SHIP123")
// .idempotencyKey(UUID.randomUUID().toString())
// .orderNumber("ORDER001")
// .build();

// Order order2 = Order.builder()
// .appUser(appUser4)
// .shop(shop1)
// .shippingAddress(appUser4.getAddresses().get(0))
// .status(OrderStatus.CONFIRMED)
// .subTotal(BigDecimal.valueOf(700000))
// .totalAmount(BigDecimal.valueOf(720000)) // + shipping
// .shippingRateId("SHIP456")
// .idempotencyKey(UUID.randomUUID().toString())
// .orderNumber("ORDER002")
// .build();

// Order order3 = Order.builder()
// .appUser(appUser2)
// .shop(shop3)
// .shippingAddress(appUser2.getAddresses().get(0))
// .status(OrderStatus.DELIVERED)
// .subTotal(BigDecimal.valueOf(450000))
// .totalAmount(BigDecimal.valueOf(450000))
// .shippingRateId("SHIP789")
// .idempotencyKey(UUID.randomUUID().toString())
// .orderNumber("ORDER003")
// .build();

// Order order4 = Order.builder()
// .appUser(appUser5)
// .shop(shop1)
// .shippingAddress(appUser5.getAddresses().get(0))
// .status(OrderStatus.DELIVERED)
// .subTotal(BigDecimal.valueOf(650000))
// .totalAmount(BigDecimal.valueOf(670000))
// .shippingRateId("SHIP101")
// .idempotencyKey(UUID.randomUUID().toString())
// .orderNumber("ORDER004")
// .build();

// Order order5 = Order.builder()
// .appUser(appUser3)
// .shop(shop3)
// .shippingAddress(appUser3.getAddresses().get(0))
// .status(OrderStatus.CANCELLED)
// .subTotal(BigDecimal.valueOf(150000))
// .totalAmount(BigDecimal.valueOf(150000))
// .shippingRateId("SHIP112")
// .idempotencyKey(UUID.randomUUID().toString())
// .orderNumber("ORDER005")
// .build();

// Order order6 = Order.builder()
// .appUser(appUser4)
// .shop(shop1)
// .shippingAddress(appUser4.getAddresses().get(0))
// .status(OrderStatus.PENDING)
// .subTotal(BigDecimal.valueOf(950000))
// .totalAmount(BigDecimal.valueOf(970000))
// .shippingRateId("SHIP131")
// .idempotencyKey(UUID.randomUUID().toString())
// .orderNumber("ORDER006")
// .build();

// orderRepository.saveAll(Arrays.asList(order1, order2, order3, order4, order5,
// order6));

// // 8. OrderItems - Thêm nhiều items (tổng 10, mỗi order có 1-3 items)
// // Order1: 1 item
// OrderItemId orderItemId1_1 = new OrderItemId();
// orderItemId1_1.setOrderId(order1.getOrderId());
// orderItemId1_1.setProductVariantId(variant1.getProductVariantId());

// OrderItem orderItem1_1 = OrderItem.builder()
// .orderItemId(orderItemId1_1)
// .commissionFeePercent(BigDecimal.TEN)
// .order(order1)
// .productVariant(variant1)
// .quantity(1)
// .price(variant1.getPrice())
// .build();

// // Order2: 2 items
// OrderItemId orderItemId2_1 = new OrderItemId();
// orderItemId2_1.setOrderId(order2.getOrderId());
// orderItemId2_1.setProductVariantId(variant2.getProductVariantId());

// OrderItem orderItem2_1 = OrderItem.builder()
// .orderItemId(orderItemId2_1)
// .commissionFeePercent(BigDecimal.TEN)

// .order(order2)
// .productVariant(variant2)
// .quantity(1)
// .price(variant2.getPrice())
// .build();

// OrderItemId orderItemId2_2 = new OrderItemId();
// orderItemId2_2.setOrderId(order2.getOrderId());
// orderItemId2_2.setProductVariantId(variant4.getProductVariantId());

// OrderItem orderItem2_2 = OrderItem.builder()
// .orderItemId(orderItemId2_2)
// .commissionFeePercent(BigDecimal.TEN)

// .order(order2)
// .productVariant(variant4)
// .quantity(1)
// .price(variant4.getPrice())
// .build();

// // Order3: 1 item
// OrderItemId orderItemId3_1 = new OrderItemId();
// orderItemId3_1.setOrderId(order3.getOrderId());
// orderItemId3_1.setProductVariantId(variant3.getProductVariantId());

// OrderItem orderItem3_1 = OrderItem.builder()
// .orderItemId(orderItemId3_1)
// .commissionFeePercent(BigDecimal.TEN)

// .order(order3)
// .productVariant(variant3)
// .quantity(2) // Quantity >1 for testing
// .price(variant3.getPrice())
// .build();

// // Order4: 3 items
// OrderItemId orderItemId4_1 = new OrderItemId();
// orderItemId4_1.setOrderId(order4.getOrderId());
// orderItemId4_1.setProductVariantId(variant5.getProductVariantId());

// OrderItem orderItem4_1 = OrderItem.builder()
// .orderItemId(orderItemId4_1)
// .commissionFeePercent(BigDecimal.TEN)

// .order(order4)
// .productVariant(variant5)
// .quantity(1)
// .price(variant5.getPrice())
// .build();

// OrderItemId orderItemId4_2 = new OrderItemId();
// orderItemId4_2.setOrderId(order4.getOrderId());
// orderItemId4_2.setProductVariantId(variant7.getProductVariantId());

// OrderItem orderItem4_2 = OrderItem.builder()
// .orderItemId(orderItemId4_2)
// .order(order4)
// .commissionFeePercent(BigDecimal.TEN)

// .productVariant(variant7)
// .quantity(1)
// .price(variant7.getPrice())
// .build();

// OrderItemId orderItemId4_3 = new OrderItemId();
// orderItemId4_3.setOrderId(order4.getOrderId());
// orderItemId4_3.setProductVariantId(variant1_extra.getProductVariantId());

// OrderItem orderItem4_3 = OrderItem.builder()
// .orderItemId(orderItemId4_3)
// .order(order4)
// .commissionFeePercent(BigDecimal.TEN)

// .productVariant(variant1_extra)
// .quantity(1)
// .price(variant1_extra.getPrice())
// .build();

// // Order6: 2 items (skip order5 as CANCELLED for testing)
// OrderItemId orderItemId6_1 = new OrderItemId();
// orderItemId6_1.setOrderId(order6.getOrderId());
// orderItemId6_1.setProductVariantId(variant2_extra.getProductVariantId());

// OrderItem orderItem6_1 = OrderItem.builder()
// .orderItemId(orderItemId6_1)
// .order(order6)
// .commissionFeePercent(BigDecimal.TEN)

// .productVariant(variant2_extra)
// .quantity(2)
// .price(variant2_extra.getPrice())
// .build();

// OrderItemId orderItemId6_2 = new OrderItemId();
// orderItemId6_2.setOrderId(order6.getOrderId());
// orderItemId6_2.setProductVariantId(variant6.getProductVariantId());

// OrderItem orderItem6_2 = OrderItem.builder()
// .orderItemId(orderItemId6_2)
// .commissionFeePercent(BigDecimal.TEN)

// .order(order6)
// .productVariant(variant6)
// .quantity(1)
// .price(variant6.getPrice())
// .build();

// orderItemRepository.saveAll(Arrays.asList(
// orderItem1_1, orderItem2_1, orderItem2_2, orderItem3_1,
// orderItem4_1, orderItem4_2, orderItem4_3, orderItem6_1, orderItem6_2));

// // Gắn OrderItems vào Orders (cho các orders có items)
// order1.setOrderItems(Arrays.asList(orderItem1_1));
// order2.setOrderItems(Arrays.asList(orderItem2_1, orderItem2_2));
// order3.setOrderItems(Arrays.asList(orderItem3_1));
// order4.setOrderItems(Arrays.asList(orderItem4_1, orderItem4_2,
// orderItem4_3));
// order6.setOrderItems(Arrays.asList(orderItem6_1, orderItem6_2));
// orderRepository.saveAll(Arrays.asList(order1, order2, order3, order4,
// order6));

// System.out.println("✅ Seeder data created successfully with expanded data for
// algorithms!");
// }

// private AppUser createUserIfNotExists(String username, String name, String
// email, String rawPassword,
// List<String> roleNames, String avatarUrl) {
// if (appUserRepository.findByUsername(username).isEmpty()) {
// AppUser appUser = AppUser.builder()
// .username(username)
// .name(name)
// .email(email)
// .avatarUrl(avatarUrl)
// .password(passwordEncoder.encode(rawPassword))
// .status(UserStatus.ACTIVE)
// .build();

// // Thêm các vai trò
// roleNames.forEach(roleStr -> {
// RoleName roleName = RoleName.valueOf(roleStr.toUpperCase());
// Role role = roleRepository.findByRoleName(roleName)
// .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò: " +
// roleName));

// UserRole userRole = UserRole.builder()
// .user(appUser)
// .role(role)
// .grantedBy("SYSTEM")
// .build();

// appUser.getUserRoles().add(userRole);
// });

// Address addr = Address.builder()
// .appUser(appUser)
// .recipientName(name)
// .phoneNumber("0123456789")
// .street("123 Đường A")
// .ward("Phường Bến Nghé")
// .district("Quận 1")
// .city("Hồ Chí Minh")
// .country("Vietnam")
// .isDefault(true)
// .build();

// appUser.getAddresses().add(addr);

// AppUser newAppUser = appUserRepository.save(appUser);
// addr.setAppUser(appUser);
// addressRepository.save(addr);

// return newAppUser;
// }
// return appUserRepository.findByUsername(username).get();
// }

// private void createRoleIfNotExists(String roleNameStr) {
// RoleName roleName = RoleName.valueOf(roleNameStr.toUpperCase());
// if (roleRepository.findByRoleName(roleName).isEmpty()) {
// Role role = new Role(roleName);
// roleRepository.save(role);
// System.out.println("✅ Seeded role: " + roleName);
// }
// }

// private Shop createShopIfNotExists(String username, String name, String
// phone, String email, String taxCode,
// String address, String description, String logoUrl, boolean isMall) {
// Optional<AppUser> userOpt = appUserRepository.findByUsername(username);
// if (userOpt.isEmpty()) {
// throw new RuntimeException("Không tìm thấy người dùng với username: " +
// username);
// }

// AppUser user = userOpt.get();

// if (shopRepository.findByAppUser_Id(user.getId()).isEmpty()) {
// Shop shop = new Shop();
// shop.setTaxCode(taxCode);
// shop.setAppUser(user);
// shop.setName(name);
// shop.setPhone(phone);
// shop.setEmail(email);
// shop.setDescription(description);
// shop.setLogoUrl(logoUrl);
// shop.setMall(isMall);
// shop.setStatus(Shop.Status.ACTIVE);
// shop.setCreatedAt(LocalDateTime.now());
// shop.setUpdatedAt(LocalDateTime.now());

// ShopAddress shopAddress = ShopAddress.builder()
// .shop(shop)
// .street(address)
// .ward("Phường Bến Nghé")
// .district("Quận 1")
// .city("Hồ Chí Minh")
// .country("Việt Nam")
// .senderName(name)
// .senderPhone(phone)
// .build();

// shop.setAddress(shopAddress);

// System.out.println(
// "✅ Đã tạo cửa hàng: " + name + " cho username: " + username + " với user_id:
// " + user.getId());
// shopRepository.save(shop);
// }

// return shopRepository.findByAppUser_Id(user.getId()).get();
// }
// }
