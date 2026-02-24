package com.example.commercepilot.product.dto.response;

import com.example.commercepilot.category.entity.Category;
import com.example.commercepilot.product.entity.Product;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProductUpdateResponse(
        Long id,
        String productName,
        Long categoryId,
        String category,
        Long price,
        LocalDateTime createAt,
        LocalDateTime modifiedAt
) {
    public static ProductUpdateResponse from(Product product) {

        // getCategory() 중복 호출을 피하기 위해 변수 분리
        Category cat = product.getCategory();

        return  ProductUpdateResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .categoryId(cat.getId())
                .category(cat.getName())
                .price(product.getPrice())
                .createAt(product.getCreatedAt())
                .modifiedAt(product.getModifiedAt())
                .build();
    }
}
