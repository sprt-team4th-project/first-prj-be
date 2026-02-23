package com.example.commercepilot.customer.controller;

import com.example.commercepilot.customer.dto.CustomerListResponseDto;
import org.springframework.data.domain.Page;
import com.example.commercepilot.customer.entity.Customer;
import com.example.commercepilot.customer.entity.CustomerStatus;
import com.example.commercepilot.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }
    @GetMapping
    public ResponseEntity<Page<CustomerListResponseDto>> getCustomers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    )
    {

        Page<CustomerListResponseDto> result =
                customerService.getCustomers(page, size);

        return ResponseEntity.ok(result);
    }
    @GetMapping("/{id}")
    public GetCustomerResponseDto getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }
}
