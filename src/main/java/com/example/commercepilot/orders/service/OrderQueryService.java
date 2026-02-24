package com.example.commercepilot.orders.service;

import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import com.example.commercepilot.orders.dto.request.OrderSearchRequest;
import com.example.commercepilot.orders.dto.response.OrderDetailResponse;
import com.example.commercepilot.orders.dto.response.OrderListResponse;
import com.example.commercepilot.orders.entity.Order;
import com.example.commercepilot.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public Page<OrderListResponse> getOrders(OrderSearchRequest request) {
        Sort sort = request.getSortDir().equalsIgnoreCase("asc")
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();

        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), sort);

        return orderRepository.findByKeywordAndStatus(
                        request.getKeyword(),
                        request.getStatus(),
                        pageable)
                .map(OrderListResponse::from);
    }

    public OrderDetailResponse getOrderDetail(Long orderId) {
        Order order = orderRepository.findByIdWithDetails(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
        return OrderDetailResponse.from(order);
    }
}
