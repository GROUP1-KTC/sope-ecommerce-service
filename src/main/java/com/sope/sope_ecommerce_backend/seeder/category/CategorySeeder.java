package com.sope.sope_ecommerce_backend.seeder.category;

import com.sope.sope_ecommerce_backend.entities.Category;
import com.sope.sope_ecommerce_backend.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CategorySeeder {

      private final CategoryRepository categoryRepository;

      public void run() {
            // Cấp 1
            Category menFashion = createCategory("Thời trang nam", null);
            Category womenFashion = createCategory("Thời trang nữ", null);
            Category phoneAccessory = createCategory("Điện thoại & Phụ kiện", null);
            Category kidFashion = createCategory("Thời trang trẻ em", null);
            Category homeAppliance = createCategory("Thiết bị gia dụng", null);
            Category computerLaptop = createCategory("Computer & Laptop", null);

            // Cấp 2
            Category jacket = createCategory("Áo khoác", menFashion);
            Category aoThun = createCategory("Áo thun", menFashion);
            Category quanNu = createCategory("Quần", womenFashion);
            Category aoVest = createCategory("Áo Vest", womenFashion);
            Category vayCuoi = createCategory("Váy cưới", womenFashion);

            // Cấp 3
            createCategory("Áo khoác mùa đông", jacket);
      }

      private Category createCategory(String name, Category parent) {
            Optional<Category> existing = categoryRepository.findByName(name);
            if (existing.isPresent()) {
                  return existing.get();
            }

            // Tạo category tạm
            Category category = new Category();
            category.setName(name);
            category.setParent(parent);
            category = categoryRepository.save(category); // Lưu để có id

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
