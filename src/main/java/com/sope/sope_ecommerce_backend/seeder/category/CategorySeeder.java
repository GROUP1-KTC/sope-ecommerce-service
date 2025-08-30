package com.sope.sope_ecommerce_backend.seeder.category;

import com.sope.sope_ecommerce_backend.entities.Category;
import com.sope.sope_ecommerce_backend.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CategorySeeder {

      private final CategoryRepository categoryRepository;

      public void run() {
            // ===== Cấp 1 =====
            Category menFashion = createCategory("Thời trang nam", null, 1, new BigDecimal("5.5"));
            Category womenFashion = createCategory("Thời trang nữ", null, 1, new BigDecimal("5.5"));
            Category phoneAccessory = createCategory("Điện thoại & Phụ kiện", null, 1, new BigDecimal("8.5"));
            Category kidFashion = createCategory("Thời trang trẻ em", null, 1, new BigDecimal("5.5"));
            Category homeAppliance = createCategory("Thiết bị gia dụng", null, 1, new BigDecimal("10.0"));
            Category computerLaptop = createCategory("Computer & Laptop", null, 1, new BigDecimal("8.5"));

            // ===== Cấp 2 =====
            Category jacket = createCategory("Áo khoác", menFashion, 2, new BigDecimal("5.5"));
            Category aoThun = createCategory("Áo thun", menFashion, 2, new BigDecimal("5.5"));
            Category quanNu = createCategory("Quần", womenFashion, 2, new BigDecimal("5.5"));
            Category aoVest = createCategory("Áo Vest", womenFashion, 2, new BigDecimal("5.5"));
            Category vayCuoi = createCategory("Váy cưới", womenFashion, 2, new BigDecimal("8.5"));

            // ===== Cấp 3 =====
            createCategory("Áo khoác mùa đông", jacket, 3, new BigDecimal("5.5"));
      }

      private Category createCategory(String name, Category parent, int level, BigDecimal commissionFeePercent) {
            Optional<Category> existing = categoryRepository.findByName(name);
            if (existing.isPresent()) {
                  return existing.get();
            }

            // Tạo category mới
            Category category = new Category();
            category.setName(name);
            category.setParent(parent);
            category.setLevel(level);
            category.setCommissionFeePercent(commissionFeePercent);

            category = categoryRepository.save(category); // Lưu lần đầu để lấy id

            // Tạo slug
            String slug = generateSlug(name, category, parent);
            category.setSlug(slug);

            return categoryRepository.save(category); // Cập nhật lại slug
      }

      private String generateSlug(String name, Category category, Category parent) {
            String nameSlug = toSlug(name);
            String categoryIdShort = category.getId().toString().substring(0, 8);

            if (parent == null) {
                  return nameSlug + "-" + categoryIdShort;
            } else {
                  String parentSlugPart = parent.getId().toString().substring(0, 8);
                  return nameSlug + "-" + parentSlugPart + "." + categoryIdShort;
            }
      }

      private String toSlug(String input) {
            String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            return normalized.toLowerCase(Locale.ROOT)
                        .replaceAll("[^a-z0-9]+", "-")
                        .replaceAll("(^-|-$)", "");
      }
}
