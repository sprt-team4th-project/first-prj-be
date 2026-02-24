package com.example.commercepilot.product.entity;

import lombok.Getter;

@Getter
public enum ProductStatus {
    ON_SALE("판매중"), // 판매중
    SOLD_OUT("품절"), // 품절
    DISCONTINUED ("단종"); // 단종

    private final String label;
    ProductStatus(String label) {
        this.label = label;
    }
}