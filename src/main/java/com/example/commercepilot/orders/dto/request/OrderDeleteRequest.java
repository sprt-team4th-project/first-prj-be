package com.example.commercepilot.orders.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OrderDeleteRequest(
        @NotBlank(message = "취소 사유는 필수입니다.")
        String cancelText
) {
}
