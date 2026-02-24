package com.example.commercepilot.orders.dto.session;

import com.example.commercepilot.admin.entity.Admin;
import com.example.commercepilot.customer.entity.Customer;

public record SessionResult(
        Customer customer, Admin admin
) {
}
