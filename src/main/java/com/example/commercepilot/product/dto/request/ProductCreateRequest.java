package com.example.commercepilot.product.dto.request;

import com.example.commercepilot.product.entity.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// 상품 등록 요청 시 입력 정보 "상품명, 카테고리, 가격, 재고, 상태"
public record ProductCreateRequest(

        @NotBlank(message = "상품명은 필수값입니다.")
        @Size(min = 1, message = "상품명은 비어있을 수 없습니다.")
        String productName,

        @NotNull(message = "카테고리ID는 필수값입니다.")
        Long categoryId,

        @NotNull(message = "가격은 필수값입니다.")
        @Min(value = 1, message = "가격은 1원 이상이어야 합니다.")
        Long price,

        @Min(value = 1, message = "재고는 1개 이상이어야 합니다.")
        int stock,

        @NotNull(message = "재고상태는 필수값입니다.")
        ProductStatus status
) {
}
