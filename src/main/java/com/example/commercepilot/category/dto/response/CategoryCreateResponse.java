package com.example.commercepilot.category.dto.response;

import com.example.commercepilot.category.entity.Category;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CategoryCreateResponse(
        Long categoryId,
        String categoryName,
        String parentCategoryName,
        LocalDateTime createdAt
) {

    public static CategoryCreateResponse from(Category category) {
        return CategoryCreateResponse.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .parentCategoryName(category.getParent() != null
                        ? category.getParent().getName()
                        : null)
                .createdAt(category.getCreatedAt())
                .build();
    }
}
