package com.example.commercepilot.customer.controller;

import com.example.commercepilot.customer.dto.request.CustomerStatusRequest;
import com.example.commercepilot.customer.dto.request.CustomerUpdateRequest;
import com.example.commercepilot.customer.dto.response.CustomerDeleteResponse;
import com.example.commercepilot.customer.dto.response.CustomerStatusResponse;
import com.example.commercepilot.customer.dto.response.CustomerUpdateResponse;
import com.example.commercepilot.customer.service.CustomerService;
import com.example.commercepilot.exception.ApiResponse;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import com.example.commercepilot.orders.dto.session.SessionAdmin;
import com.example.commercepilot.orders.dto.session.SessionCustomer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    // 고객 정보 수정 (본인만)
    @PatchMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerUpdateResponse>> updateCustomer(
            @PathVariable Long customerId,
            @SessionAttribute(name = "loginCustomer", required = false) SessionCustomer sessionCustomer,
            @Valid @RequestBody CustomerUpdateRequest request
    ) {
        if (sessionCustomer == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        if (!sessionCustomer.customerId().equals(customerId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, customerService.update(customerId, request)));
    }

    // 고객 상태 변경 (관리자만)
    @PatchMapping("/{customerId}/status")
    public ResponseEntity<ApiResponse<CustomerStatusResponse>> updateStatus(
            @PathVariable Long customerId,
            @SessionAttribute(name = "loginAdmin", required = false) SessionAdmin sessionAdmin,
            @Valid @RequestBody CustomerStatusRequest request
    ) {
        if (sessionAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, customerService.updateStatus(customerId, request)));
    }

    // 고객 삭제 (관리자만)
    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerDeleteResponse>> deleteCustomer(
            @PathVariable Long customerId,
            @SessionAttribute(name = "loginAdmin", required = false) SessionAdmin sessionAdmin
    ) {
        if (sessionAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, customerService.delete(customerId)));
    }
}
