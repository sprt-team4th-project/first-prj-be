package com.example.commercepilot.product.dto.response;

import com.example.commercepilot.product.entity.Product;
import com.example.commercepilot.product.entity.ProductStatus;
import lombok.Builder;

@Builder
// 재고값 변경 응답 dto
public record StockChangeResponse(
        Long id,
        int stock,
        ProductStatus status
) {
    public static StockChangeResponse from(Product product) {

        return StockChangeResponse.builder()
                .id(product.getId())
                .stock(product.getStock())
                .status(product.getStatus())
                .build();
    }
}
