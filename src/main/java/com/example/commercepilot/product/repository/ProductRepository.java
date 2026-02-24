package com.example.commercepilot.product.repository;

import com.example.commercepilot.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}