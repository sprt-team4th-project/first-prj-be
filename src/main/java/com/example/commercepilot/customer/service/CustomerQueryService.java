package com.example.commercepilot.customer.service;

import com.example.commercepilot.customer.dto.request.CustomerSearchRequest;
import com.example.commercepilot.customer.dto.response.CustomerListResponse;
import com.example.commercepilot.customer.entity.Customer;
import com.example.commercepilot.customer.repository.CustomerRepository;
import com.example.commercepilot.customer.repository.CustomerSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerQueryService {

    private final CustomerRepository customerRepository;

    public Page<CustomerListResponse> getCustomers(CustomerSearchRequest request) {
        Sort sort = request.getSortDir().equalsIgnoreCase("asc")
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        Specification<Customer> spec = Specification
                .where(CustomerSpecifications.keyword(request.getKeyword()))
                .and(CustomerSpecifications.status(request.getStatus()));

        return customerRepository.findAll(spec, pageable)
                .map(CustomerListResponse::from);
    }
}
