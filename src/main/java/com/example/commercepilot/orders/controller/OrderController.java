package com.example.commercepilot.orders.controller;

import com.example.commercepilot.exception.ApiResponse;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import com.example.commercepilot.orders.dto.request.OrderCreateRequest;
import com.example.commercepilot.orders.dto.request.OrderDeleteRequest;
import com.example.commercepilot.orders.dto.request.OrderSearchRequest;
import com.example.commercepilot.orders.dto.response.OrderCreateResponse;
import com.example.commercepilot.orders.dto.response.OrderDeleteResponse;
import com.example.commercepilot.orders.dto.response.OrderListResponse;
import com.example.commercepilot.orders.dto.response.OrderUpdateResponse;
import com.example.commercepilot.orders.dto.session.SessionAdmin;
import com.example.commercepilot.orders.dto.session.SessionCustomer;
import com.example.commercepilot.orders.service.OrderQueryService;
import com.example.commercepilot.orders.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderQueryService orderQueryService;
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderCreateResponse>> createOrder(
            @SessionAttribute(name = "loginAdmin", required = false) SessionAdmin sessionAdmin,
            @SessionAttribute(name = "loginCustomer", required = false) SessionCustomer sessionCustomer,
            @Valid @RequestBody OrderCreateRequest request
    ) {
        if (sessionAdmin == null && sessionCustomer == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, orderService.add(sessionAdmin, sessionCustomer, request)));
    }


    @GetMapping
    public ResponseEntity<Page<OrderListResponse>> getOrders(
            @Valid @ModelAttribute OrderSearchRequest request) {
        return ResponseEntity.ok(orderQueryService.getOrders(request));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderUpdateResponse>> updateOrder(
            @PathVariable Long orderId,
            @SessionAttribute(name = "loginAdmin", required = false) SessionAdmin sessionAdmin
    ) {
        if (sessionAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, orderService.update(orderId)));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDeleteResponse>> deleteOrder(
            @PathVariable Long orderId,
            @SessionAttribute(name = "loginAdmin", required = false) SessionAdmin sessionAdmin,
            @SessionAttribute(name = "loginCustomer", required = false) SessionCustomer sessionCustomer,
            @Valid @RequestBody OrderDeleteRequest request
    ) {
        if (sessionAdmin == null && sessionCustomer == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, orderService.delete(orderId, sessionAdmin, sessionCustomer, request)));
    }
}
