// package com.sope.sope_ecommerce_backend.seeder;

// import com.opencsv.CSVReader;

// import com.sope.sope_ecommerce_backend.modules.product.entity.*;
// import com.sope.sope_ecommerce_backend.modules.product.enums.StatusProduct;
// import com.sope.sope_ecommerce_backend.modules.product.repository.*;
// import com.sope.sope_ecommerce_backend.modules.user.entity.Shop;
// import com.sope.sope_ecommerce_backend.modules.user.entity.User;
// import com.sope.sope_ecommerce_backend.modules.user.enums.Gender;
// import
// com.sope.sope_ecommerce_backend.modules.user.repository.ShopRepository;
// import
// com.sope.sope_ecommerce_backend.modules.user.repository.UserRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;

// import java.io.FileReader;
// import java.math.BigDecimal;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.util.*;

// @Component
// @RequiredArgsConstructor
// public class DataSeeder implements CommandLineRunner {

// private final UserRepository userRepository;
// private final ShopRepository shopRepository;
// private final AttributeRepository attributeRepository;
// private final ImageRepository imageRepository;
// private final ProductRepository productRepository;
// private final CategoryRepository categoryRepository;
// private final ProductVariantRepository productVariantRepository;

// @Override
// public void run(String... args) {
// seedUsers();
// seedShops();
// seedCategories();
// seedImages();
// seedAttributes();
// seedProducts();
// seedProductVariants();
// seedProductVariantAttributes();
// }

// private void seedUsers() {
// String path =
// "src/main/java/com/sope/sope_ecommerce_backend/seeder/csv/users.csv";
// try (CSVReader reader = new CSVReader(new FileReader(path))) {
// List<String[]> rows = reader.readAll();
// rows.remove(0);
// for (String[] row : rows) {
// User user = new User();
// user.setUserId(UUID.fromString(row[0]));
// user.setCreatedAt(LocalDateTime.parse(row[1]));
// user.setDateOfBirth(LocalDate.parse(row[2]));
// user.setEmail(row[3]);
// user.setFullName(row[4]);
// user.setGender(Gender.valueOf(row[5].toUpperCase()));
// user.setLocked(parseBoolean(row[6]));
// user.setPassword(row[7]);
// user.setPhone(row[8]);
// user.setSocialMediaId(emptyToNull(row[9]));
// user.setTax(emptyToNull(row[10]));
// user.setVerified(parseBoolean(row[11]));
// userRepository.save(user);
// }
// System.out.println("✅ Seeded users");
// } catch (Exception e) {
// e.printStackTrace();
// }
// }

// private void seedShops() {
// String path =
// "src/main/java/com/sope/sope_ecommerce_backend/seeder/csv/shops.csv";
// try (CSVReader reader = new CSVReader(new FileReader(path))) {
// List<String[]> rows = reader.readAll();
// rows.remove(0);
// for (String[] row : rows) {
// Shop shop = new Shop();
// shop.setShopId(UUID.fromString(row[0]));
// shop.setActive(parseBoolean(row[1]));
// shop.setAddress(row[2]);
// shop.setCreatedAt(LocalDateTime.parse(row[3]));
// shop.setDescription(row[4]);
// shop.setIsMall(parseBoolean(row[5]));
// shop.setLogoUrl(row[6]);
// shop.setName(row[7]);
// shop.setUpdatedAt(LocalDateTime.parse(row[8]));
// // set owner
// userRepository.findById(UUID.fromString(row[9])).ifPresent(shop::setOwner);
// shopRepository.save(shop);
// }
// System.out.println("✅ Seeded shops");
// } catch (Exception e) {
// e.printStackTrace();
// }
// }

// private void seedImages() {
// try {
// List<String> lines = Files.readAllLines(
// Paths.get("src/main/java/com/sope/sope_ecommerce_backend/seeder/csv/images.csv"));
// lines.remove(0);
// for (String line : lines) {
// String[] row = line.split(",");
// Image image = new Image();
// image.setImageId(Long.parseLong(row[0]));
// image.setPriority(Integer.parseInt(row[1]));
// image.setUrl(row[2]);
// productVariantRepository.findById(UUID.fromString(row[3])).ifPresent(image::setProductVariant);
// imageRepository.save(image);
// }
// System.out.println("✅ Seeded images");
// } catch (Exception e) {
// e.printStackTrace();
// }
// }

// private void seedAttributes() {
// try {
// List<String> lines = Files.readAllLines(
// Paths.get("src/main/java/com/sope/sope_ecommerce_backend/seeder/csv/attributes.csv"));
// lines.remove(0);
// for (String line : lines) {
// String[] row = line.split(",");
// Attribute attribute = new Attribute();
// attribute.setAttributeId(Long.parseLong(row[0]));
// attribute.setName(row[1]);
// attribute.setValue(row[2]);
// if (!row[3].isBlank()) {
// imageRepository.findById(Long.parseLong(row[3])).ifPresent(attribute::setImage);
// }
// attributeRepository.save(attribute);
// }
// System.out.println("✅ Seeded attributes");
// } catch (Exception e) {
// e.printStackTrace();
// }
// }

// private void seedCategories() {
// try {
// List<String> lines = Files.readAllLines(
// Paths.get("src/main/java/com/sope/sope_ecommerce_backend/seeder/csv/categories.csv"));
// lines.remove(0);
// for (String line : lines) {
// String[] row = line.split(",", -1);
// Category category = new Category();
// category.setName(row[1]);
// category.setSlug(row[2]);
// if (!row[3].isBlank()) {
// categoryRepository.findById(UUID.fromString(row[3])).ifPresent(category::setParent);
// }
// categoryRepository.save(category);
// }
// System.out.println("✅ Seeded categories");
// } catch (Exception e) {
// e.printStackTrace();
// }
// }

// private void seedProducts() {
// try {
// List<String> lines = Files.readAllLines(
// Paths.get("src/main/java/com/sope/sope_ecommerce_backend/seeder/csv/products.csv"));
// lines.remove(0);
// for (String line : lines) {
// String[] row = line.split(",", -1);
// Product product = new Product();
// product.setProductId(UUID.fromString(row[0]));
// product.setBrand(row[1]);
// product.setCreatedAt(LocalDateTime.parse(row[2]));
// product.setDefaultImage(row[3]);
// product.setDefaultPrice(new BigDecimal(row[4]));
// product.setDescription(row[5]);
// product.setHidden(parseBoolean(row[6]));
// product.setName(row[7]);
// product.setSlug(row[8]);
// product.setStatus(StatusProduct.valueOf(row[9].toUpperCase()));
// if (!row[10].isBlank()) {
// product.setUpdatedAt(LocalDateTime.parse(row[10]));
// }
// categoryRepository.findById(UUID.fromString(row[11])).ifPresent(product::setCategory);
// shopRepository.findById(UUID.fromString(row[12])).ifPresent(product::setShop);
// productRepository.save(product);
// }
// System.out.println("✅ Seeded products");
// } catch (Exception e) {
// e.printStackTrace();
// }
// }

// private void seedProductVariants() {
// try {
// List<String> lines = Files.readAllLines(Paths
// .get("src/main/java/com/sope/sope_ecommerce_backend/seeder/csv/product_variants.csv"));
// lines.remove(0);
// for (String line : lines) {
// String[] row = line.split(",", -1);
// ProductVariant variant = new ProductVariant();
// variant.setProductVariantId(UUID.fromString(row[0]));
// variant.setCreatedAt(LocalDateTime.parse(row[1]));
// variant.setHidden(parseBoolean(row[2]));
// variant.setPrice(new BigDecimal(row[3]));
// variant.setSlug(row[4]);
// variant.setSold(Integer.parseInt(row[5]));
// variant.setStock(Integer.parseInt(row[6]));
// if (!row[7].isBlank()) {
// variant.setUpdatedAt(LocalDateTime.parse(row[7]));
// }
// productRepository.findById(UUID.fromString(row[8])).ifPresent(variant::setProduct);
// productVariantRepository.save(variant);
// }
// System.out.println("✅ Seeded product variants");
// } catch (Exception e) {
// e.printStackTrace();
// }
// }

// private void seedProductVariantAttributes() {
// try {
// List<String> lines = Files.readAllLines(Paths.get(
// "src/main/java/com/sope/sope_ecommerce_backend/seeder/csv/product_variant_attribute.csv"));
// lines.remove(0);
// for (String line : lines) {
// String[] row = line.split(",", -1);
// UUID variantId = UUID.fromString(row[0]);
// Long attributeId = Long.parseLong(row[1]);

// productVariantRepository.findById(variantId).ifPresent(variant -> {
// attributeRepository.findById(attributeId).ifPresent(attribute -> {
// variant.getAttributes().add(attribute);
// productVariantRepository.save(variant);
// });
// });
// }
// System.out.println("✅ Seeded product variant attributes");
// } catch (Exception e) {
// e.printStackTrace();
// }
// }

// private Boolean parseBoolean(String value) {
// return value != null && (value.equalsIgnoreCase("true") ||
// value.equalsIgnoreCase("t"));
// }

// private String emptyToNull(String s) {
// return (s == null || s.trim().isEmpty()) ? null : s;
// }
// }
