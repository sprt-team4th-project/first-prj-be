package com.example.commercepilot.customer.repository;

import com.example.commercepilot.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerRepository extends JpaRepository<Customer, Long>,
        JpaSpecificationExecutor<Customer> {

    boolean existsByEmailAndIdNot(String email, Long id);
}
