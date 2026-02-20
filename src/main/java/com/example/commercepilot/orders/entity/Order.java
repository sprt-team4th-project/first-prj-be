package com.example.commercepilot.orders.entity;

import com.example.commercepilot.admin.entity.Admin;
import com.example.commercepilot.config.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Entity
@Table(name = "orders")
@SoftDelete(columnName = "is_deleted")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 20)
    private String orderNumber;
    private Long totalPrice;
    private int quantity;
    private String cancelText;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @PrePersist
    public void createOrderNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = UUID.randomUUID().toString().toUpperCase().substring(0, 8);

        this.orderNumber = datePart + "-" + randomPart;
    }

    public Order(Long totalPrice, int quantity, Customer customer, Product product, Admin admin, OrderStatus status) {
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.customer = customer;
        this.product = product;
        this.admin = admin;
        this.status = status;
    }

    public void changeStatus(OrderStatus orderStatus) {
        this.status = orderStatus;
    }

    public void cancel(String cancelText) {
        this.status = OrderStatus.CANCELLED;
        this.cancelText = cancelText;
    }
}



