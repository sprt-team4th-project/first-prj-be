package com.example.commercepilot.admin.dto.request;

import com.example.commercepilot.admin.entity.AdminStatus;
import jakarta.validation.constraints.NotNull;

public record AdminStatusChangeRequest(
        @NotNull(message = "상태는 필수입니다.")
        AdminStatus status
) {}