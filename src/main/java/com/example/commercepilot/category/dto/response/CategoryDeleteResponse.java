package com.example.commercepilot.category.dto.response;

import com.example.commercepilot.category.entity.Category;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CategoryDeleteResponse(
        Long id,
        String name,
        LocalDateTime deletedAt
) {

    public static CategoryDeleteResponse from(Category category) {
        return CategoryDeleteResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .deletedAt(category.getDeletedAt())
                .build();
    }
}
