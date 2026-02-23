package com.example.commercepilot.customer.repository;

import com.example.commercepilot.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByEmailAndIdNot(String email, Long id);
}
