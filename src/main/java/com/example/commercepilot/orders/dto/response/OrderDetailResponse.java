package com.example.commercepilot.orders.dto.response;

import com.example.commercepilot.admin.entity.AdminRole;
import com.example.commercepilot.orders.entity.Order;
import com.example.commercepilot.orders.entity.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderDetailResponse {

    private final String orderNumber;
    private final String customerName;
    private final String customerEmail;
    private final String productName;
    private final int quantity;
    private final Long totalPrice;
    private final LocalDateTime orderedAt;
    private final OrderStatus status;
    private final String adminName;
    private final String adminEmail;
    private final AdminRole adminRole;

    private OrderDetailResponse(Order order) {
        this.orderNumber = order.getOrderNumber();
        this.customerName = order.getCustomer().getName();
        this.customerEmail = order.getCustomer().getEmail();
        this.productName = order.getProduct().getName();
        this.quantity = order.getQuantity();
        this.totalPrice = order.getTotalPrice();
        this.orderedAt = order.getCreatedAt();
        this.status = order.getStatus();
        this.adminName = order.getAdmin() != null ? order.getAdmin().getAdminName() : null;
        this.adminEmail = order.getAdmin() != null ? order.getAdmin().getEmail() : null;
        this.adminRole = order.getAdmin() != null ? order.getAdmin().getRole() : null;
    }

    public static OrderDetailResponse from(Order order) {
        return new OrderDetailResponse(order);
    }
}
