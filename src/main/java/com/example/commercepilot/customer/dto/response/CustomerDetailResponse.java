package com.example.commercepilot.customer.dto.response;

import com.example.commercepilot.customer.entity.Customer;
import com.example.commercepilot.customer.entity.CustomerStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CustomerDetailResponse(
        Long id, String customerName,
        String email, String callNumber,
        CustomerStatus status, LocalDateTime createdAt
) {

    public static CustomerDetailResponse from(Customer customer) {
        return CustomerDetailResponse.builder()
                .id(customer.getId())
                .customerName(customer.getCustomerName())
                .email(customer.getEmail())
                .callNumber(customer.getCallNumber())
                .status(customer.getStatus())
                .createdAt(customer.getCreatedAt())
                .build();
    }
}
