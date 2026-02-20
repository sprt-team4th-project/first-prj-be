package com.example.commercepilot.product.controller;

import com.example.commercepilot.exception.ApiResponse;
import com.example.commercepilot.product.dto.request.ProductCreateRequest;
import com.example.commercepilot.product.dto.request.ProductSearchRequest;
import com.example.commercepilot.product.dto.request.ProductUpdateRequest;
import com.example.commercepilot.product.dto.response.ProductCreateResponse;
import com.example.commercepilot.product.dto.response.ProductListResponse;
import com.example.commercepilot.product.dto.response.ProductUpdateResponse;
import com.example.commercepilot.product.service.ProductCommandService;
import com.example.commercepilot.product.service.ProductQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
//- 카테고리 필터 (예: 전자기기, 패션/의류, 식품)
public class ProductController {
    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    @PostMapping // 상품 생성
    public ResponseEntity<ApiResponse<ProductCreateResponse>> createProduct(
            @SessionAttribute
            @Valid @RequestBody ProductCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, productCommandService.createProduct(request)));
    }

    @PatchMapping("/{productId}") // 상품 수정 (상품명, 카테고리, 가격, 재고)
    public ResponseEntity<ApiResponse<ProductUpdateResponse>> updateProduct(
            @Valid @RequestBody ProductUpdateRequest request,
            @PathVariable Long productId) {
        return ResponseEntity.ok
                (ApiResponse.success(HttpStatus.OK, productCommandService.updateProduct(request, productId)));
    }

    @GetMapping("/{productId}") // 상품 단건 조회
    public ResponseEntity<ApiResponse<ProductListResponse>> getProduct(
            @PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, productQueryService.getProduct(productId)));
    }

    @GetMapping // 상품 전체 조회
    public ResponseEntity<ApiResponse<Page<ProductListResponse>>> getProducts(
            @Valid @ModelAttribute ProductSearchRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, productQueryService.getProducts(request)));
    }

    @DeleteMapping("/{productId}") // 상품 단건 삭제
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long productId) {
        productCommandService.deleteProduct(productId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK));
    }
}
