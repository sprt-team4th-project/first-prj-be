package com.example.commercepilot.admin.dto.request;

import com.example.commercepilot.admin.entity.AdminRole;
import com.example.commercepilot.admin.entity.AdminStatus;

public record AdminSearchCondition(
        String keyword,     // name/email
        AdminRole role,
        AdminStatus status
) {}
