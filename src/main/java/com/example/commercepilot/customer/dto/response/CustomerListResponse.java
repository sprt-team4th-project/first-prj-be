package com.example.commercepilot.customer.dto.response;

import com.example.commercepilot.customer.entity.Customer;
import com.example.commercepilot.customer.entity.CustomerStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CustomerListResponse {

    private final Long id;
    private final String customerName;
    private final String email;
    private final String callNumber;
    private final CustomerStatus status;
    private final LocalDateTime createdAt;

    private CustomerListResponse(Customer customer) {
        this.id = customer.getId();
        this.customerName = customer.getCustomerName();
        this.email = customer.getEmail();
        this.callNumber = customer.getCallNumber();
        this.status = customer.getStatus();
        this.createdAt = customer.getCreatedAt();
    }

    public static CustomerListResponse from(Customer customer) {
        return new CustomerListResponse(customer);
    }
}
