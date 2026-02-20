package com.example.commercepilot.orders.dto.response;

import com.example.commercepilot.orders.entity.Order;
import com.example.commercepilot.orders.entity.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderListResponse {

    private final Long id;
    private final String orderNumber;
    private final String customerName;
    private final String productName;
    private final int quantity;
    private final Long totalPrice;
    private final LocalDateTime orderedAt;
    private final OrderStatus status;
    private final String adminName;

    private OrderListResponse(Order order) {
        this.id = order.getId();
        this.orderNumber = order.getOrderNumber();
        this.customerName = order.getCustomer().getName();
        this.productName = order.getProductName();
        this.quantity = order.getQuantity();
        this.totalPrice = order.getTotalPrice();
        this.orderedAt = order.getCreatedAt();
        this.status = order.getStatus();
        this.adminName = order.getAdminName();
    }

    public static OrderListResponse from(Order order) {
        return new OrderListResponse(order);
    }
}
