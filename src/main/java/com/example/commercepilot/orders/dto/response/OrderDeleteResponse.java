package com.example.commercepilot.orders.dto.response;

import com.example.commercepilot.orders.entity.Order;
import lombok.Builder;

@Builder
public record OrderDeleteResponse(
        Long orderId,
        String productName,
        int quantity,
        Long totalPrice
) {

    public static OrderDeleteResponse from(Order order) {
        return OrderDeleteResponse.builder()
                .orderId(order.getId())
                .productName(order.getProduct().getProductName())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
