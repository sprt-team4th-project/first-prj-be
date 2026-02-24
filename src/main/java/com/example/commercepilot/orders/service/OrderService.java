package com.example.commercepilot.orders.service;

import com.example.commercepilot.admin.dto.session.LoginAdmin;
import com.example.commercepilot.admin.entity.Admin;
import com.example.commercepilot.admin.entity.AdminRole;
import com.example.commercepilot.admin.repository.AdminRepository;
import com.example.commercepilot.customer.entity.Customer;
import com.example.commercepilot.customer.repository.CustomerRepository;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import com.example.commercepilot.orders.dto.request.OrderCreateRequest;
import com.example.commercepilot.orders.dto.request.OrderDeleteRequest;
import com.example.commercepilot.orders.dto.response.OrderCreateResponse;
import com.example.commercepilot.orders.dto.response.OrderDeleteResponse;
import com.example.commercepilot.orders.dto.response.OrderUpdateResponse;
import com.example.commercepilot.customer.dto.session.LoginCustomer;
import com.example.commercepilot.orders.dto.session.SessionResult;
import com.example.commercepilot.orders.entity.Order;
import com.example.commercepilot.orders.entity.OrderStatus;
import com.example.commercepilot.orders.repository.OrderRepository;
import com.example.commercepilot.product.entity.Product;
import com.example.commercepilot.product.entity.ProductStatus;
import com.example.commercepilot.product.repository.ProductRepository;
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
    public OrderCreateResponse add(LoginAdmin loginAdmin, LoginCustomer loginCustomer, OrderCreateRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        validateProduct(product, request);

        long totalPrice = request.quantity() * product.getPrice();

        SessionResult sessionResult = resolveSession(loginAdmin, loginCustomer, request.customerId());
        Customer customer = sessionResult.customer();
        Admin admin = sessionResult.admin();

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
    public OrderDeleteResponse delete(Long orderId, LoginAdmin loginAdmin, LoginCustomer loginCustomer, OrderDeleteRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        Long customerId = order.getCustomer().getId();

        if (!loginCustomer.customerId().equals(customerId)) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        resolveSession(loginAdmin, loginCustomer, customerId);

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new CustomException(ErrorCode.ORDER_CANCEL_NOT_ALLOWED);
        }

        Product product = order.getProduct();
        product.increaseStock(order.getQuantity());

        order.cancel(request.cancelText());

        return OrderDeleteResponse.from(order);
    }

    private SessionResult resolveSession(
            LoginAdmin loginAdmin, LoginCustomer loginCustomer, Long customerId) {

        if (loginCustomer != null) {
            Customer customer = customerRepository.findById(loginCustomer.customerId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));
            return new SessionResult(customer, null);
        }

        if (loginAdmin == null) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        if (loginAdmin.role() != AdminRole.CS_ADMIN) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        if (customerId == null) {
            throw new CustomException(ErrorCode.CUSTOMER_ID_REQUIRED);
        }

        Admin admin = adminRepository.findById(loginAdmin.adminId())
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));

        return new SessionResult(customer, admin);
    }
}
