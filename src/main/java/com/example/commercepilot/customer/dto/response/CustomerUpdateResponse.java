package com.example.commercepilot.customer.dto.response;

import com.example.commercepilot.customer.entity.Customer;
import lombok.Getter;

@Getter
public class CustomerUpdateResponse {

    private final Long id;
    private final String customerName;
    private final String email;
    private final String callNumber;

    private CustomerUpdateResponse(Customer customer) {
        this.id = customer.getId();
        this.customerName = customer.getCustomerName();
        this.email = customer.getEmail();
        this.callNumber = customer.getCallNumber();
    }

    public static CustomerUpdateResponse from(Customer customer) {
        return new CustomerUpdateResponse(customer);
    }
}
