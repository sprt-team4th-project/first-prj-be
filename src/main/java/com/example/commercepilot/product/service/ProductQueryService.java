package com.example.commercepilot.product.service;

import com.example.commercepilot.product.dto.request.ProductSearchRequest;
import com.example.commercepilot.product.dto.response.ProductListResponse;
import com.example.commercepilot.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductQueryService {
    private final ProductRepository productRepository;


    public Page<ProductListResponse> getProducts(ProductSearchRequest request) {

    }

    public ProductListResponse getProduct(Long productId) {
    }
}
