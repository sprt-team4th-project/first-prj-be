package com.example.commercepilot.customer.dto.response;

import com.example.commercepilot.customer.entity.Customer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerListResponseDto {

    private final Long id;
    private final String customerName;
    private final String email;
    private final String callNumber;
    private final String status;
    private final LocalDateTime createdAt;

    public CustomerListResponseDto(Customer customer) {
        this.id = customer.getId();
        this.customerName = customer.getCustomerName();
        this.email = customer.getEmail();
        this.callNumber = customer.getCallNumber();
        this.status = customer.getStatus().name();
        this.createdAt = customer.getCreatedAt();
    }
}
