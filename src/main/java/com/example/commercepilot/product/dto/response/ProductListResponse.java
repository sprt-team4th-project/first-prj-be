package com.example.commercepilot.product.dto.response;

import com.example.commercepilot.product.entity.Product;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
// 고유 식별자(ID), 상품명, 카테고리, 가격, 재고, 상태, 등록일, 등록 관리자명
public record ProductListResponse(
        Long id,
        String productName,
        String category,
        Long price,
        int stock,
        String status,
        LocalDateTime createdAt,
        String adminName) {
    public static ProductListResponse from(Product product) {
        return ProductListResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .category(product.getCategory().getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus().getLabel())
                .createdAt(product.getCreatedAt())
                .adminName(product.getAdmin().getAdminName())
                .build();
    }
}