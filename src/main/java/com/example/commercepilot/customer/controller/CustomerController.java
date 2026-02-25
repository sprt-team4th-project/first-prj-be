package com.example.commercepilot.customer.controller;

import com.example.commercepilot.admin.dto.session.LoginAdmin;
import com.example.commercepilot.customer.dto.request.CustomerSearchRequest;
import com.example.commercepilot.customer.dto.request.CustomerStatusRequest;
import com.example.commercepilot.customer.dto.request.CustomerUpdateRequest;
import com.example.commercepilot.customer.dto.response.*;
import com.example.commercepilot.customer.service.CustomerQueryService;
import com.example.commercepilot.customer.service.CustomerService;
import com.example.commercepilot.exception.ApiResponse;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.commercepilot.admin.config.SessionConst.LOGIN_ADMIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")

public class CustomerController {

    private final CustomerService customerService;
    private final CustomerQueryService customerQueryService;

    // 고객 목록 조회 (관리자만)
    @GetMapping
    public ResponseEntity<Page<CustomerListResponse>> getCustomers(
            @SessionAttribute(name = LOGIN_ADMIN, required = false) LoginAdmin loginAdmin,
            @Valid @ModelAttribute CustomerSearchRequest request
    ) {
        if (loginAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return ResponseEntity.ok(customerQueryService.getCustomers(request));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerDetailResponse>> getCustomer(
            @PathVariable Long customerId,
            @SessionAttribute(name = LOGIN_ADMIN, required = false) LoginAdmin loginAdmin) {

        if (loginAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, customerQueryService.getCustomer(customerId)));
    }

    // 고객 정보 수정 (관리자만)
    @PatchMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerUpdateResponse>> updateCustomer(
            @PathVariable Long customerId,
            @SessionAttribute(name = LOGIN_ADMIN, required = false) LoginAdmin loginAdmin,
            @Valid @RequestBody CustomerUpdateRequest request
    ) {
        if (loginAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, customerService.update(customerId, request)));
    }

    // 고객 상태 변경 (관리자만)
    @PatchMapping("/{customerId}/status")
    public ResponseEntity<ApiResponse<CustomerStatusResponse>> updateStatus(
            @PathVariable Long customerId,
            @SessionAttribute(name = LOGIN_ADMIN, required = false) LoginAdmin loginAdmin,
            @Valid @RequestBody CustomerStatusRequest request
    ) {
        if (loginAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, customerService.updateStatus(customerId, request)));
    }

    // 고객 삭제 (관리자만)
    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerDeleteResponse>> deleteCustomer(
            @PathVariable Long customerId,
            @SessionAttribute(name = LOGIN_ADMIN, required = false) LoginAdmin loginAdmin
    ) {
        if (loginAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, customerService.delete(customerId)));
    }
}
