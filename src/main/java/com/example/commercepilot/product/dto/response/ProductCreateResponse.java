package com.example.commercepilot.product.dto.response;

import com.example.commercepilot.product.entity.Product;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
// 고유 ID, 등록관리자명, 등록일 추가로 응답받음
public record ProductCreateResponse(
        Long id,
        String productName,
        String category,
        Long price,
        String adminName,
        int stock,
        String status,
        LocalDateTime createdAt) {

    public static ProductCreateResponse from(Product product) {
        return ProductCreateResponse.builder()
                .id(product.getId())
                .adminName(product.getAdmin().getAdminName())
                .productName(product.getProductName())
                .category(product.getCategory().getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus().getLabel())
                .createdAt(product.getCreatedAt())
                .build();

    }
}
