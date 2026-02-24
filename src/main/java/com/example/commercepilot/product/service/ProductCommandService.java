package com.example.commercepilot.product.service;

import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import com.example.commercepilot.product.dto.request.ProductCreateRequest;
import com.example.commercepilot.product.dto.request.ProductUpdateRequest;
import com.example.commercepilot.product.dto.response.ProductCreateResponse;
import com.example.commercepilot.product.dto.response.ProductListResponse;
import com.example.commercepilot.product.dto.response.ProductUpdateResponse;
import com.example.commercepilot.product.entity.Product;
import com.example.commercepilot.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Service
@RequiredArgsConstructor
//쓰기전용
public class ProductCommandService {

    private final ProductRepository productRepository;

    public ProductCreateResponse createProduct(ProductCreateRequest request) {
        Product product = new Product(
                request.getProductName(),
                request.getPrice(),
                request.getStock(),
                request.getStatus(),
                category
        );

        Product savedProduct = productRepository.save(product);

        return new ProductCreateResponse(
                savedProduct.getId(),
                savedProduct.getProductName(),
                savedProduct.getPrice(),
                savedProduct.getStock(),
                savedProduct.getStatus(),
                savedProduct.getCreatedAt()
        );
    }

    @Transactional
    public ProductUpdateResponse updateProduct(ProductUpdateRequest request, Long productId) {
        throw new CustomException(ErrorCode.
    }

    public ProductListResponse getOne(Long productId) {

    }


    public List<ProductListResponse> getAll() {

    }

    @Transactional
    public void deleteProduct(Long productId) {

    }
}
}
