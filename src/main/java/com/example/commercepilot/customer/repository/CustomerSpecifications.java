package com.example.commercepilot.customer.repository;

import com.example.commercepilot.customer.entity.Customer;
import com.example.commercepilot.customer.entity.CustomerStatus;
import org.springframework.data.jpa.domain.Specification;

public final class CustomerSpecifications {

    private CustomerSpecifications() {}

    // 이름 또는 이메일로 검색
    public static Specification<Customer> keyword(String keyword) {
        if (keyword == null || keyword.isBlank()) return null;
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("customerName")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%")
        );
    }

    // 상태로 필터링
    public static Specification<Customer> status(CustomerStatus status) {
        if (status == null) return null;
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
