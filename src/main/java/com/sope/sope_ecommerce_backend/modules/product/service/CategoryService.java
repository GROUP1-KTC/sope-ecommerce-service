package com.sope.sope_ecommerce_backend.modules.product.service;

import com.github.slugify.Slugify;
import com.sope.sope_ecommerce_backend.modules.product.dto.CategoryCreateRequest;
import com.sope.sope_ecommerce_backend.modules.product.dto.CategoryResponse;
import com.sope.sope_ecommerce_backend.modules.product.entity.Category;
import com.sope.sope_ecommerce_backend.modules.product.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

      @Autowired
      private CategoryRepository categoryRepository;

      public CategoryResponse createCategory(CategoryCreateRequest request) {
            Category category = new Category();
            category.setName(request.getName());

            // Bước 1: Xử lý parent nếu có
            Category parent = null;
            if (request.getParentId() != null) {
                  parent = categoryRepository.findById(request.getParentId())
                              .orElseThrow(() -> new RuntimeException("Parent not found"));
                  category.setParent(parent);
            }

            // Bước 2: Lưu tạm để có UUID
            category = categoryRepository.save(category);

            // Bước 3: Tạo slug
            Slugify slugify = Slugify.builder().lowerCase(true).build();
            String nameSlug = slugify.slugify(category.getName());
            String shortId = category.getId().toString().substring(0, 8);

            String slug;

            if (parent != null) {
                  // Tách phần "cat.ancestor_ids" từ slug của cha
                  String parentSlug = parent.getSlug(); // ví dụ: ao-khoac-cat.29ff1169.c619af8d
                  String slugSuffix = parentSlug.substring(parentSlug.indexOf("cat.")); // "cat.29ff1169.c619af8d"
                  slug = nameSlug + "-" + slugSuffix + "." + shortId;
            } else {
                  slug = nameSlug + "-cat." + shortId;
            }

            category.setSlug(slug);

            // Bước 4: lưu lại slug
            category = categoryRepository.save(category);

            return toResponse(category);
      }

      public List<CategoryResponse> getAllCategories() {
            return categoryRepository.findAll().stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList());
      }

      public List<Category> getAllDescendantCategories(Category parent) {
            List<Category> result = new ArrayList<>();
            result.add(parent); // bao gồm chính nó

            List<Category> allCategories = categoryRepository.findAll();
            findChildrenRecursive(parent, allCategories, result);

            return result;
      }

      private void findChildrenRecursive(Category parent, List<Category> allCategories, List<Category> result) {
            for (Category category : allCategories) {
                  if (category.getParent() != null && category.getParent().getId().equals(parent.getId())) {
                        result.add(category);
                        findChildrenRecursive(category, allCategories, result);
                  }
            }
      }

      private CategoryResponse toResponse(Category category) {
            CategoryResponse res = new CategoryResponse();
            res.setId(category.getId());
            res.setName(category.getName());
            res.setSlug(category.getSlug());

            if (category.getParent() != null) {
                  CategoryResponse.ParentInfo parentDto = new CategoryResponse.ParentInfo();
                  parentDto.setId(category.getParent().getId());
                  parentDto.setName(category.getParent().getName());
                  res.setParent(parentDto);
            } else {
                  res.setParent(null);
            }

            return res;
      }
}
