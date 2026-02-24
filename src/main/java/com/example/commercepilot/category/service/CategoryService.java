package com.example.commercepilot.category.service;

import com.example.commercepilot.category.dto.request.CategoryCreateRequest;
import com.example.commercepilot.category.dto.request.CategoryUpdateRequest;
import com.example.commercepilot.category.dto.response.*;
import com.example.commercepilot.category.entity.Category;
import com.example.commercepilot.category.repository.CategoryRepository;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryCreateResponse add(CategoryCreateRequest request) {
        if (categoryRepository.existsByName(request.categoryName())) {
            throw new CustomException(ErrorCode.DUPLICATE_CATEGORY_NAME);
        }

        Category parentCategory = findParentCategory(request.parentCategoryId());
        Category category = new Category(request.categoryName(), parentCategory);
        Category savedCategory = categoryRepository.save(category);

        return CategoryCreateResponse.from(savedCategory);
    }

    public Page<CategoryListResponse> getCategories(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAllWithParent(pageable);

        return CategoryListResponse.from(categories);
    }

    public CategoryResponse getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        return CategoryResponse.from(category);
    }

    @Transactional
    public CategoryResponse update(Long categoryId, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Category parentCategory = findParentCategory(request.parentCategoryId());

        category.rename(request.categoryName());
        category.changeParent(parentCategory);

        return CategoryResponse.from(category);
    }

    @Transactional
    public void restore(Long categoryId) {
        Category category = categoryRepository.findDeletedById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        category.restore();
    }

    @Transactional
    public CategoryDeleteResponse delete(Long categoryId) {
        Category category = categoryRepository.findByIdWithChildren(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        category.deleteWithDescendants();

        return CategoryDeleteResponse.from(category);
    }

    private Category findParentCategory(Long parentId) {
        if (parentId == null) return null;
        return categoryRepository.findById(parentId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public CategoryDeleteResponse getDeletedCategories() {
        return null;
    }
}
