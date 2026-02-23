package com.example.commercepilot.orders.dto.response;

import com.example.commercepilot.orders.entity.Order;
import com.example.commercepilot.orders.entity.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrderCreateResponse(
        Long id, OrderStatus status, Long totalPrice, LocalDateTime createdAt
) {

    public static OrderCreateResponse from(Order order) {
        return OrderCreateResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
