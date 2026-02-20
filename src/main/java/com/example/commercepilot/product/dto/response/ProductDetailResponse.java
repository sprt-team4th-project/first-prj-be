package com.example.commercepilot.product.dto.response;

import com.example.commercepilot.product.entity.Product;
import lombok.Builder;

import java.time.LocalDateTime;

// 특정 상품의 상세 정보를 조회합니다.
// `상품명`, `카테고리`, `가격`, `재고`, `상태`, `등록일`,  `등록 관리자명`, `등록 관리자 이메일`
// 존재하지 않는 ID 요청 시 에러를 반환합니다.
@Builder
public record ProductDetailResponse(
        Long id,
        String productName,
        String category,
        Long price,
        int stock,
        String status,
        LocalDateTime createdAt,
        String adminName,
        String adminEmail) {

    public static ProductDetailResponse from(Product product) {
        return ProductDetailResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .category(product.getCategory().getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .status(product.getStatus().getLabel())
                .createdAt(product.getCreatedAt())
                .adminName(product.getAdmin().getAdminName())
                .adminEmail(product.getAdmin().getEmail())
                .build();
    }
}

