package com.example.commercepilot.customer.dto.response;

import com.example.commercepilot.customer.entity.Customer;
import com.example.commercepilot.customer.entity.CustomerStatus;
import lombok.Getter;

@Getter
public class CustomerStatusResponse {

    private final Long id;
    private final CustomerStatus status;

    private CustomerStatusResponse(Customer customer) {
        this.id = customer.getId();
        this.status = customer.getStatus();
    }

    public static CustomerStatusResponse from(Customer customer) {
        return new CustomerStatusResponse(customer);
    }
}
