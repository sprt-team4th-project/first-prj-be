package com.example.commercepilot.product.controller;

import com.example.commercepilot.exception.ApiResponse;
import com.example.commercepilot.product.dto.request.*;
import com.example.commercepilot.product.dto.response.*;
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

public class ProductController {
    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    @PostMapping // 상품 생성
    public ResponseEntity<ApiResponse<ProductCreateResponse>> createProduct(
            @SessionAttribute(name = "loginAdmin", required = false) SessionAdmin sessionAdmin,
            @Valid @RequestBody ProductCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, productCommandService.createProduct(sessionAdmin, request)));
    }

    @PatchMapping("/{productId}") // 상품 수정 (상품명, 카테고리, 가격, 재고)
    public ResponseEntity<ApiResponse<ProductUpdateResponse>> updateProduct(
            @SessionAttribute(name = "loginAdmin", required = false) SessionAdmin sessionAdmin,
            @Valid @RequestBody ProductUpdateRequest request,
            @PathVariable Long productId) {
        return ResponseEntity.ok
                (ApiResponse.success(HttpStatus.OK, productCommandService.updateProduct(sessionAdmin, request, productId)));
    }

    @GetMapping("/{productId}") // 상품 상세 조회
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductDetail(
            @PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK, productQueryService.getProductDetail(productId)));
    }

    @GetMapping // 상품 전체 조회
    public ResponseEntity<ApiResponse<Page<ProductListResponse>>> getProductList(
            @Valid @ModelAttribute ProductSearchRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, productQueryService.getProductList(request)));
    }

    @DeleteMapping("/{productId}") // 상품 단건 삭제
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @SessionAttribute(name = "loginAdmin", required = false) SessionAdmin sessionAdmin,
            @PathVariable Long productId) {
        productCommandService.deleteProduct(sessionAdmin, productId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK));
    }

    @PatchMapping("/{productId}/stock") // 재고 값 변경
    public ResponseEntity<ApiResponse<StockChangeResponse>> changeStock(
            @SessionAttribute(name = "loginAdmin", required = false) SessionAdmin sessionAdmin,
            @PathVariable Long productId,
            @RequestBody StockChangeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,
                productCommandService.updateStock(sessionAdmin, productId, request.newStock())));
    }

    @PatchMapping("/{productId}/stock/increase") // 기존 재고 추가
    public ResponseEntity<ApiResponse<StockChangeResponse>> increaseStock(
            @SessionAttribute(name = "loginAdmin", required = false) SessionAdmin sessionAdmin,
            @PathVariable Long productId,
            @RequestBody StockAmountRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,
                productCommandService.increaseStock(sessionAdmin, productId, request.amount())));
    }

    @PatchMapping("/{productId}/stock/decrease") // 기존 재고 감소
    public ResponseEntity<ApiResponse<StockChangeResponse>> decreaseStock(
            @SessionAttribute(name = "loginAdmin", required = false) SessionAdmin sessionAdmin,
            @PathVariable Long productId,
            @RequestBody StockAmountRequest request) {
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK,
                productCommandService.decreaseStock(sessionAdmin, productId, request.amount())));
    }

    @PatchMapping("/{productId}/status/discontinue") // 재고상태 단종으로 변경
    public ResponseEntity<ApiResponse<Void>> discontinue(
            @SessionAttribute(name = "loginAdmin", required = false) SessionAdmin sessionAdmin,
            @PathVariable Long productId
    ) {
        productCommandService.discontinuedProduct(sessionAdmin, productId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK));
    }
}

