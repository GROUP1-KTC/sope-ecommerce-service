package com.sope.sope_ecommerce_backend.services.impl;

import com.github.slugify.Slugify;
import com.sope.sope_ecommerce_backend.dto.request.CategoryCreateDTO;
import com.sope.sope_ecommerce_backend.dto.response.CategoryDTO;
import com.sope.sope_ecommerce_backend.entities.CategoryEntity;
import com.sope.sope_ecommerce_backend.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl {

      @Autowired
      private CategoryRepository categoryRepository;

      public CategoryDTO createCategory(CategoryCreateDTO request) {
            CategoryEntity category = new CategoryEntity();
            category.setName(request.getName());

            CategoryEntity parent = null;
            if (request.getParentId() != null) {
                  parent = categoryRepository.findById(request.getParentId())
                              .orElseThrow(() -> new RuntimeException("Parent not found"));
                  category.setParent(parent);
            }

            category = categoryRepository.save(category);

            Slugify slugify = Slugify.builder().lowerCase(true).build();
            String nameSlug = slugify.slugify(category.getName());
            String shortId = category.getId().toString().substring(0, 8);

            String slug;

            if (parent != null) {
                  String parentSlug = parent.getSlug(); // ví dụ: ao-khoac-cat.29ff1169.c619af8d
                  String slugSuffix = parentSlug.substring(parentSlug.indexOf("cat.")); // "cat.29ff1169.c619af8d"
                  slug = nameSlug + "-" + slugSuffix + "." + shortId;
            } else {
                  slug = nameSlug + "-cat." + shortId;
            }

            category.setSlug(slug);

            category = categoryRepository.save(category);

            return toResponse(category);
      }

      public List<CategoryDTO> getAllCategories() {
            return categoryRepository.findAll().stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList());
      }

      private CategoryDTO toResponse(CategoryEntity category) {
            CategoryDTO res = new CategoryDTO();
            res.setId(category.getId());
            res.setName(category.getName());
            res.setSlug(category.getSlug());

            if (category.getParent() != null) {
                  CategoryDTO.ParentInfo parentDto = new CategoryDTO.ParentInfo();
                  parentDto.setId(category.getParent().getId());
                  parentDto.setName(category.getParent().getName());
                  res.setParent(parentDto);
            } else {
                  res.setParent(null);
            }

            return res;
      }
}
