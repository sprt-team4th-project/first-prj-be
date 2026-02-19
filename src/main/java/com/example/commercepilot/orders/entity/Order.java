package com.example.commercepilot.orders.entity;

import com.example.commercepilot.config.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@SoftDelete(columnName = "isdeleted")
@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalPrice;

    private Customer customer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPING, DELIVERED, CANCELLED
    }
}
