package com.example.commercepilot.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// 상품 등록 요청 시 입력 정보 "상품명, 카테고리, 가격, 재고, 상태"
public record ProductCreateRequest(
        @NotBlank(message = "상품명은 필수값입니다.")
        @Size(max = 30, message = "30자 이내로 작성해주세요.")
        String productName,
        @NotBlank(message = "카테고리명은 필수값입니다.")
        @Size(max = 20, message = "20자 이내로 작성해주세요.")
        String category,
        @NotBlank(message = "가격은 필수값입니다.")
        Long price,
        @NotBlank(message = "재고는 필수값입니다.")
        int stock,
        @NotBlank(message = "재고상태는 필수값입니다.")
        String status
) {
}
