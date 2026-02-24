package com.example.commercepilot.category.dto.response;

import com.example.commercepilot.category.entity.Category;
import lombok.Builder;
import org.springframework.data.domain.Page;

@Builder
public record CategoryListResponse(
        Long id,
        String categoryName,
        String parentCategoryName
) {

    public static CategoryListResponse from(Category category) {
        return CategoryListResponse.builder()
                .id(category.getId())
                .categoryName(category.getName())
                .parentCategoryName(category.getParent() != null
                        ? category.getParent().getName()
                        : null)
                .build();
    }

    public static Page<CategoryListResponse> from(Page<Category> categories) {
        return categories.map(CategoryListResponse::from);
    }
}