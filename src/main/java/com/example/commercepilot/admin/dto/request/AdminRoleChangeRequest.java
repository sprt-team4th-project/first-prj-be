package com.example.commercepilot.admin.dto.request;

import com.example.commercepilot.admin.entity.AdminRole;
import jakarta.validation.constraints.NotNull;

public record AdminRoleChangeRequest(
        @NotNull(message = "역할은 필수입니다.")
        AdminRole role
) {}