package com.example.commercepilot.orders.service;

import com.example.commercepilot.admin.entity.Admin;
import com.example.commercepilot.admin.entity.AdminRole;
import com.example.commercepilot.admin.repository.AdminRepository;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import com.example.commercepilot.orders.dto.request.OrderCreateRequest;
import com.example.commercepilot.orders.dto.request.OrderDeleteRequest;
import com.example.commercepilot.orders.dto.response.OrderCreateResponse;
import com.example.commercepilot.orders.dto.response.OrderDeleteResponse;
import com.example.commercepilot.orders.dto.response.OrderUpdateResponse;
import com.example.commercepilot.orders.dto.session.SessionAdmin;
import com.example.commercepilot.orders.dto.session.SessionCustomer;
import com.example.commercepilot.orders.entity.Order;
import com.example.commercepilot.orders.entity.OrderStatus;
import com.example.commercepilot.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public OrderCreateResponse add(SessionAdmin sessionAdmin, SessionCustomer sessionCustomer, OrderCreateRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        validateProduct(product, request);

        long totalPrice = request.quantity() * product.getPrice();

        Customer customer;
        Admin admin = null;

        if (sessionCustomer != null) {
            customer = customerRepository.findById(sessionCustomer.customerId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));
        } else {
            if (sessionAdmin.role() != AdminRole.CS_ADMIN) {
                throw new CustomException(ErrorCode.FORBIDDEN);
            }
            if (request.customerId() == null) {
                throw new CustomException(ErrorCode.CUSTOMER_ID_REQUIRED);
            }
            admin = adminRepository.findById(sessionAdmin.adminId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));
            customer = customerRepository.findById(request.customerId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));
        }

        product.decreaseStock(request.quantity());

        Order order = new Order(totalPrice, request.quantity(), customer, product, admin, OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);

        return OrderCreateResponse.from(savedOrder);
    }

    private void validateProduct(Product product, OrderCreateRequest request) {
        ProductStatus productStatus = product.getStatus();

        if (productStatus == ProductStatus.DISCONTINUED) {
            throw new CustomException(ErrorCode.PRODUCT_DISCONTINUED);
        }
        if (productStatus == ProductStatus.SOLD_OUT) {
            throw new CustomException(ErrorCode.PRODUCT_SOLD_OUT);
        }
        if (product.getStock() < request.quantity()) {
            throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
        }
    }

    @Transactional
    public OrderUpdateResponse update(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.proceed();

        return OrderUpdateResponse.from(order);
    }

    @Transactional
    public OrderDeleteResponse delete(Long orderId, SessionAdmin sessionAdmin, SessionCustomer sessionCustomer, OrderDeleteRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        Long customerId = order.getCustomer().getId();

        if (sessionCustomer != null && !sessionCustomer.customerId().equals(customerId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new CustomException(ErrorCode.ORDER_CANCEL_NOT_ALLOWED);
        }

        Product product = order.getProduct();
        product.addStock(order.getQuantity());

        order.cancel(request.cancelText());

        return OrderDeleteResponse.from(order);
    }
}
