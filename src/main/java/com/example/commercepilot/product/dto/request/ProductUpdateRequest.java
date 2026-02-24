package com.example.commercepilot.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequest(
        // 수정 가능한 필드: "상품명, 카테고리, 가격" + 재고를 자동처리할지 여기서 처리할지 고민중
        @NotBlank(message = "상품명은 필수값입니다.")
        @Size(max = 30, message = "30자 이내로 작성해주세요.")
        String productName,
        @NotBlank(message = "카테고리명은 필수값입니다.")
        String category,
        @NotBlank(message = "가격은 필수값입니다.")
        Long price
) {
}
