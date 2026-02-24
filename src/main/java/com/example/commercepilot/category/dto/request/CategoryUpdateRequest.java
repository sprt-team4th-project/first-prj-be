package com.example.commercepilot.category.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryUpdateRequest(
        Long parentCategoryId,
        @NotBlank(message = "카테고리명은 필수입니다.")
        String categoryName
) {
}
