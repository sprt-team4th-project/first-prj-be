package com.example.commercepilot.product.dto.response;

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
        return new ProductUpdateResponse(
                product.getId(),
                product.getProductName(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getPrice(),
                product.getCreatedAt(),
                product.getModifiedAt()
        );
    }
}
