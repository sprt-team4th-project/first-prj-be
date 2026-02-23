package com.example.commercepilot.admin.repository;

import com.example.commercepilot.admin.entity.Admin;
import com.example.commercepilot.admin.entity.AdminRole;
import com.example.commercepilot.admin.entity.AdminStatus;
import org.springframework.data.jpa.domain.Specification;

public final class AdminSpecifications {

    private AdminSpecifications() {}

    public static Specification<Admin> keyword(String keyword) {
        if (keyword == null || keyword.isBlank()) return null;

        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("adminName")), "%" + keyword.toLowerCase() + "%"),
                cb.like(cb.lower(root.get("email")), "%" + keyword.toLowerCase() + "%")
        );
    }

    public static Specification<Admin> role(AdminRole role) {
        if (role == null) return null;
        return (root, query, cb) -> cb.equal(root.get("role"), role);
    }

    public static Specification<Admin> status(AdminStatus status) {
        if (status == null) return null;
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }
}
