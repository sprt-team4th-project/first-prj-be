package com.example.commercepilot.product.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequest(

        @Size(min = 1, message = "상품명은 비어있을 수 없습니다.")
        String productName,

        // 카테고리 명은 카테고리파트에서 변경 가능, 상품파트에서는 그저 존재하는 다른 카테고리로 옮긴단 느낌
        Long categoryId,

        @Min(value = 1, message = "가격은 1원 이상이어야 합니다.")
        Long price
) {
}
