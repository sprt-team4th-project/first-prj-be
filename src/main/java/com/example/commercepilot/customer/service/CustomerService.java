package com.example.commercepilot.customer.service;

import com.example.commercepilot.customer.dto.GetCustomerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.example.commercepilot.customer.dto.CustomerListResponseDto;
import com.example.commercepilot.customer.entity.Customer;
import com.example.commercepilot.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public GetCustomerResponseDto getCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 고객입니다.")
                );

        return new GetCustomerResponseDto(
                customer.getCustomerName(),
                customer.getEmail(),
                customer.getCallNumber(),
                customer.getStatus().name(),
                customer.getCreatedAt()
        );
    }
    public Page<CustomerListResponseDto> getCustomers(int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        return customerRepository.findAll(pageable)
                .map(CustomerListResponseDto::new);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

}
