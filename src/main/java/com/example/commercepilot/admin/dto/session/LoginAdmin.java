package com.example.commercepilot.admin.dto.session;

import com.example.commercepilot.admin.entity.AdminRole;

public record LoginAdmin(
        Long adminId,
        String email,
        AdminRole role
) {}