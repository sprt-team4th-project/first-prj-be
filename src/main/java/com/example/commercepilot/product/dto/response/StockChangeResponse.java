package com.example.commercepilot.product.dto.response;

import com.example.commercepilot.product.entity.Product;
import com.example.commercepilot.product.entity.ProductStatus;

// 재고값 변경 응답 dto
public record StockChangeResponse(
        Long productId,
        int stock,
        ProductStatus status
) {
    public static StockChangeResponse from(Product product) {
        return new StockChangeResponse(product.getId(), product.getStock(), product.getStatus());
    }

}
