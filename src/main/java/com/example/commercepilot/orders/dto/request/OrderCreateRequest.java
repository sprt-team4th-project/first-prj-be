package com.example.commercepilot.orders.dto.request;

import jakarta.validation.constraints.Min;

public record OrderCreateRequest(
        Long customerId,
        Long productId,
        @Min(value = 1, message = "주문 수량은 최소 1개 이상이어야 합니다.")
        int quantity
) {
}
