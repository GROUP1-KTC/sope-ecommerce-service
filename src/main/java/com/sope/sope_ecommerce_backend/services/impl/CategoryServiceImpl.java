package com.sope.sope_ecommerce_backend.services.impl;

import com.github.slugify.Slugify;
import com.sope.sope_ecommerce_backend.dto.request.CategoryCreateDTO;
import com.sope.sope_ecommerce_backend.dto.request.CategoryUpdateCommissionDTO;
import com.sope.sope_ecommerce_backend.dto.response.CategoryDTO;
import com.sope.sope_ecommerce_backend.entities.Category;
import com.sope.sope_ecommerce_backend.mapper.CategoryMapper;
import com.sope.sope_ecommerce_backend.repositories.CategoryRepository;
import com.sope.sope_ecommerce_backend.services.CategoryService;
import com.sope.sope_ecommerce_backend.services.FileUploadService;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
      private final CategoryRepository categoryRepository;
      private final CategoryMapper categoryMapper;
      private final FileUploadService fileUploadService;

      @Override
      @Transactional
      public CategoryDTO createCategory(CategoryCreateDTO request, MultipartFile imageForParent) {
            Category category = new Category();
            category.setName(request.name());
            category.setCommissionFeePercent(request.commissionFeePercent());

            Category parent = null;

            if (request.parentId() == null) {
                  category.setLevel(1);
                  category.setParent(null);
            } else {
                  parent = categoryRepository.findById(request.parentId())
                              .orElseThrow(() -> new RuntimeException("Parent not found"));
                  category.setParent(parent);
                  category.setLevel(parent.getLevel() + 1);
            }

            if (imageForParent != null && !imageForParent.isEmpty()) {
                  String imageUrl = fileUploadService.uploadImage(imageForParent);
                  category.setImageForParent(imageUrl);
            }

            category = categoryRepository.save(category);

            // Bước 3: Tạo slug
            Slugify slugify = Slugify.builder().lowerCase(true).build();
            String nameSlug = slugify.slugify(category.getName());
            String shortId = category.getId().toString().substring(0, 8);

            String slug;

            if (parent != null) {
                  String parentSlug = parent.getSlug();
                  String slugSuffix = parentSlug.substring(parentSlug.indexOf("cat."));
                  slug = nameSlug + "-" + slugSuffix + "." + shortId;
            } else {
                  slug = nameSlug + "-cat." + shortId;
            }

            category.setSlug(slug);

            // Bước 4: lưu lại slug
            category = categoryRepository.save(category);

            return categoryMapper.toDto(category);

      }

      @Override
      @Transactional(readOnly = true)
      public List<CategoryDTO> getAllCategories() {
            return categoryMapper.toDtoList(categoryRepository.findAll());
      }

      @Override
      @Transactional(readOnly = true)
      public List<CategoryDTO> getBreadcrumb(UUID categoryId) {
            List<CategoryDTO> path = new ArrayList<>();

            Category current = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("Category not found"));

            // Lần ngược lên tới root
            while (current != null) {
                  path.add(categoryMapper.toDto(current));
                  current = current.getParent();
            }

            // Đảo ngược để có root → leaf
            Collections.reverse(path);

            return path;
      }

      @Override
      @Transactional
      public CategoryDTO updateCommissionFee(UUID categoryId, CategoryUpdateCommissionDTO request) {
            Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RuntimeException("Category not found"));

            category.setCommissionFeePercent(request.commissionFeePercent());

            category = categoryRepository.save(category);
            return categoryMapper.toDto(category);
      }

}
