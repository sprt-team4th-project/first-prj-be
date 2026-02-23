package com.example.commercepilot.admin.dto.response;

import com.example.commercepilot.admin.entity.Admin;
import com.example.commercepilot.admin.entity.AdminRole;
import com.example.commercepilot.admin.entity.AdminStatus;

import java.time.LocalDateTime;

public record AdminDetailResponse(
        Long id,
        String name,
        String email,
        String callNumber,
        AdminRole role,
        AdminStatus status,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static AdminDetailResponse from(Admin admin) {
        return new AdminDetailResponse(
                admin.getId(),
                admin.getAdminName(),
                admin.getEmail(),
                admin.getCallNumber(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt(),
                admin.getModifiedAt()
        );
    }
}