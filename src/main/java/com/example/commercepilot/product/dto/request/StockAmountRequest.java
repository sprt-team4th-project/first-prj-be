package com.example.commercepilot.product.dto.request;

import jakarta.validation.constraints.Min;

public record StockAmountRequest(
        @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
        int amount
) {}
