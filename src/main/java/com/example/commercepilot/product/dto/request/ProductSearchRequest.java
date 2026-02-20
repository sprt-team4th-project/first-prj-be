package com.example.commercepilot.product.dto.request;

import com.example.commercepilot.product.entity.ProductStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchRequest {
    // 검색키워드
    private String keyword;

    // 카테고리 필터
    private Long CategoryId;

    // 상태 필터
    private ProductStatus productStatus;

    // 페이지 번호(기본값: 1)
    @Min( value = 1, message ="페이지 번호는 1 이상이어야 합니다.")
    private int page = 1;

    // 페이지당 개수(기본값: 10)
    @Max(value = 100, message = "한번에 최대 100개까지만 조회할 수 있습니다.")
    private int size = 10;

    // 등록일을 기준으로 정렬
    private String sortBy = "createdAt";

    // 내림차순으로 정렬
    private String sortDir = "desc";
}
