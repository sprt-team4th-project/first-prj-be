package com.example.commercepilot.category.dto.response;

import com.example.commercepilot.category.entity.Category;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CategoryDeleteResponse(
        Long categoryId,
        String categoryName,
        LocalDateTime deletedAt
) {

    public static CategoryDeleteResponse from(Category category) {
        return CategoryDeleteResponse.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .deletedAt(category.getDeletedAt())
                .build();
    }
}
