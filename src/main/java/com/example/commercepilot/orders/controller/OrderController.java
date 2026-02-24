package com.example.commercepilot.orders.controller;

import com.example.commercepilot.admin.dto.session.LoginAdmin;
import com.example.commercepilot.exception.ApiResponse;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import com.example.commercepilot.orders.dto.request.OrderCreateRequest;
import com.example.commercepilot.orders.dto.request.OrderDeleteRequest;
import com.example.commercepilot.orders.dto.request.OrderSearchRequest;
import com.example.commercepilot.orders.dto.response.*;
import com.example.commercepilot.customer.dto.session.LoginCustomer;
import com.example.commercepilot.orders.service.OrderQueryService;
import com.example.commercepilot.orders.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.commercepilot.admin.config.SessionConst.LOGIN_ADMIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderQueryService orderQueryService;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderCreateResponse>> createOrder(
            @SessionAttribute(name = LOGIN_ADMIN, required = false) LoginAdmin loginAdmin,
            @SessionAttribute(name = "loginCustomer", required = false) LoginCustomer loginCustomer,
            @Valid @RequestBody OrderCreateRequest request
    ) {
        if (loginAdmin == null && loginCustomer == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, orderService.add(loginAdmin, loginCustomer, request)));
    }


    @GetMapping
    public ResponseEntity<Page<OrderListResponse>> getOrders(
            @Valid @ModelAttribute OrderSearchRequest request) {
        return ResponseEntity.ok(orderQueryService.getOrders(request));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrderDetail(
            @PathVariable Long orderId) {
        return ResponseEntity.ok(
                ApiResponse.success(HttpStatus.OK, orderQueryService.getOrderDetail(orderId)));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderUpdateResponse>> updateOrder(
            @PathVariable Long orderId,
            @SessionAttribute(name = LOGIN_ADMIN, required = false) LoginAdmin loginAdmin
    ) {
        if (loginAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, orderService.update(orderId)));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDeleteResponse>> deleteOrder(
            @PathVariable Long orderId,
            @SessionAttribute(name = LOGIN_ADMIN, required = false) LoginAdmin loginAdmin,
            @SessionAttribute(name = "loginCustomer", required = false) LoginCustomer loginCustomer,
            @Valid @RequestBody OrderDeleteRequest request
    ) {
        if (loginAdmin == null && loginCustomer == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, orderService.delete(orderId, loginAdmin, loginCustomer, request)));
    }
}
