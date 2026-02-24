package com.example.commercepilot.product.dto.request;


import com.example.commercepilot.admin.entity.AdminRole;
import jakarta.validation.constraints.NotNull;

public record SessionAdmin(
        @NotNull(message = "관리자 ID는 필수입니다.")
        Long adminId,
        @NotNull(message = "관리자 역할은 필수입니다.")
        AdminRole role) {
}
