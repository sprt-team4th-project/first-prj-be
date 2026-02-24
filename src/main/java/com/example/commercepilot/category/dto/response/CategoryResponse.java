package com.example.commercepilot.category.dto.response;

import com.example.commercepilot.category.entity.Category;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CategoryResponse(
        Long id,
        String categoryName,
        String parentCategoryName,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {

    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .categoryName(category.getName())
                .parentCategoryName(category.getParent() != null
                        ? category.getParent().getName()
                        : null)
                .createdAt(category.getCreatedAt())
                .modifiedAt(category.getModifiedAt())
                .build();
    }
}
