package com.example.commercepilot.orders.controller;

import com.example.commercepilot.orders.dto.request.OrderSearchRequest;
import com.example.commercepilot.orders.dto.response.OrderListResponse;
import com.example.commercepilot.orders.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;


    @GetMapping
    public ResponseEntity<Page<OrderListResponse>> getOrders(
            @Valid @ModelAttribute OrderSearchRequest request) {
        return ResponseEntity.ok(orderService.getOrders(request));
    }
}
