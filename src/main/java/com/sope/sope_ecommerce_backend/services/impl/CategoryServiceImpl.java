package com.sope.sope_ecommerce_backend.services.impl;

import com.github.slugify.Slugify;
import com.sope.sope_ecommerce_backend.dto.request.CategoryCreateDTO;
import com.sope.sope_ecommerce_backend.dto.response.CategoryDTO;
import com.sope.sope_ecommerce_backend.entities.Category;
import com.sope.sope_ecommerce_backend.mapper.CategoryMapper;
import com.sope.sope_ecommerce_backend.repositories.CategoryRepository;
import com.sope.sope_ecommerce_backend.services.CategoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
      private final CategoryRepository categoryRepository;
      private final CategoryMapper categoryMapper;

      @Override
      @Transactional
      public CategoryDTO createCategory(CategoryCreateDTO request) {
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
            categoryRepository.save(category);

            return categoryMapper.toDto(category);

      }

      @Override
      @Transactional(readOnly = true)
      public List<CategoryDTO> getAllCategories() {
            return categoryMapper.toDtoList(categoryRepository.findAll());
      }

}
