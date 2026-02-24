package com.example.commercepilot.orders.dto.session;

import com.example.commercepilot.admin.entity.AdminRole;

public record SessionAdmin(
        Long adminId,
        AdminRole role
) {
}
