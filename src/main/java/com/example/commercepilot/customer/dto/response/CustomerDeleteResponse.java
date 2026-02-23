package com.example.commercepilot.customer.dto.response;

import com.example.commercepilot.customer.entity.Customer;
import lombok.Getter;

@Getter
public class CustomerDeleteResponse {

    private final Long id;
    private final String customerName;

    private CustomerDeleteResponse(Customer customer) {
        this.id = customer.getId();
        this.customerName = customer.getCustomerName();
    }

    public static CustomerDeleteResponse from(Customer customer) {
        return new CustomerDeleteResponse(customer);
    }
}
