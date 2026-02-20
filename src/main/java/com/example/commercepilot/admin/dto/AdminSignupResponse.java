package com.example.commercepilot.admin.dto;

import com.example.commercepilot.admin.entity.Admin;
import com.example.commercepilot.admin.entity.AdminRole;
import com.example.commercepilot.admin.entity.AdminStatus;

import java.time.LocalDateTime;

public record AdminSignupResponse(
        Long id,
        String name,
        String email,
        String callNumber,
        AdminRole role,
        AdminStatus status,
        LocalDateTime createdAt
) {
    public static AdminSignupResponse from(Admin admin) {
        return new AdminSignupResponse(
                admin.getId(),
                admin.getAdminName(),
                admin.getEmail(),
                admin.getCallNumber(),
                admin.getRole(),
                admin.getStatus(),
                admin.getCreatedAt()
        );
    }
}