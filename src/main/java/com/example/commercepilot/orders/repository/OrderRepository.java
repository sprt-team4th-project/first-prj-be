package com.example.commercepilot.orders.repository;

import com.example.commercepilot.orders.entity.Order;
import com.example.commercepilot.orders.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.customer c " +
            "WHERE (:keyword IS NULL OR o.orderNumber LIKE %:keyword% OR c.name LIKE %:keyword%) " +
            "AND (:status IS NULL OR o.status = :status)")
    Page<Order> findByKeywordAndStatus(
            @Param("keyword") String keyword,
            @Param("status") OrderStatus status,
            Pageable pageable);
}
