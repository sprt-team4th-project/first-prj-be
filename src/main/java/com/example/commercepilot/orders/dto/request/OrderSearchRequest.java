package com.example.commercepilot.orders.dto.request;

import com.example.commercepilot.orders.entity.Order;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchRequest {

    private String keyword;                          // 주문번호, 고객명 검색

    @Min(value = 1, message = "페이지 번호는 1 이상이어야 합니다.")
    private int page = 1;                            // 페이지 번호 (기본값: 1)

    @Max(value = 100, message = "한 번에 최대 100개까지만 조회할 수 있습니다.")
    private int size = 10;                           // 페이지당 개수 (기본값: 10)

    private String sortBy = "createdAt";             // 정렬 기준: quantity, totalPrice, createdAt
    private String sortDir = "desc";                 // 정렬 순서: asc, desc
    private Order.OrderStatus status;                // 상태 필터
}
