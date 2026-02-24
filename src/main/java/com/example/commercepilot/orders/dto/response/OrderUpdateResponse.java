package com.example.commercepilot.orders.dto.response;

import com.example.commercepilot.orders.entity.Order;
import com.example.commercepilot.orders.entity.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrderUpdateResponse(
        Long id, OrderStatus status, Long totalPrice, LocalDateTime createdAt, LocalDateTime modifiedAt
) {

    public static OrderUpdateResponse from(Order order) {
        return OrderUpdateResponse.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .modifiedAt(order.getModifiedAt())
                .build();
    }
}
