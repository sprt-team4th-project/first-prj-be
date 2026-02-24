package com.example.commercepilot.product.service;

import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import com.example.commercepilot.product.dto.request.ProductSearchRequest;
import com.example.commercepilot.product.dto.response.ProductDetailResponse;
import com.example.commercepilot.product.dto.response.ProductListResponse;
import com.example.commercepilot.product.entity.Product;
import com.example.commercepilot.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProductQueryService {
    private final ProductRepository productRepository;

    public Page<ProductListResponse> getProductList(ProductSearchRequest request) {

        int page = request.getPage() - 1; // 사용자 요청은 1페이지이지만 Spring Pageable은 0부터 시작하기 떄문에 -1
        int size = request.getSize(); // 한 페이지에 보여줄 상품 수

        Sort.Direction direction; // 정렬 방향 변수

        // 사용자가 asc라고 보냈는지 확인 (IgnoreCase통해 대소문자 무시)
        if ("asc".equalsIgnoreCase(request.getSortDir())) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }
        // Pageable 생성
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, request.getSortBy()));

        return productRepository.search(
                request.getKeyword(), // 검색 키워드 (상품명)
                request.getCategoryId(), // 카테고리 필터
                request.getProductStatus(), // 상태 필터
                pageable // 페이징
        ).map(ProductListResponse::from);
    }

    public ProductDetailResponse getProductDetail(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        return ProductDetailResponse.from(product);
    }
}
