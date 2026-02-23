package com.example.commercepilot.customer.service;

import com.example.commercepilot.customer.dto.request.CustomerStatusRequest;
import com.example.commercepilot.customer.dto.request.CustomerUpdateRequest;
import com.example.commercepilot.customer.dto.response.CustomerDeleteResponse;
import com.example.commercepilot.customer.dto.response.CustomerStatusResponse;
import com.example.commercepilot.customer.dto.response.CustomerUpdateResponse;
import com.example.commercepilot.customer.entity.Customer;
import com.example.commercepilot.customer.entity.CustomerStatus;
import com.example.commercepilot.customer.repository.CustomerRepository;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerRepository customerRepository;

    // 고객 정보 수정 (본인만 가능)
    @Transactional
    public CustomerUpdateResponse update(Long customerId, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));

        if (customerRepository.existsByEmailAndIdNot(request.getEmail(), customerId)) {
            throw new CustomException(ErrorCode.CUSTOMER_EMAIL_DUPLICATED);
        }

        customer.updateInfo(request.getCustomerName(), request.getEmail(), request.getCallNumber());

        return CustomerUpdateResponse.from(customer);
    }

    // 고객 상태 변경 (관리자만 가능)
    @Transactional
    public CustomerStatusResponse updateStatus(Long customerId, CustomerStatusRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));

        customer.changeStatus(request.getStatus());

        return CustomerStatusResponse.from(customer);
    }

    // 고객 삭제 (관리자만 가능)
    @Transactional
    public CustomerDeleteResponse delete(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomException(ErrorCode.CUSTOMER_NOT_FOUND));

        customer.changeStatus(CustomerStatus.DELETED);

        return CustomerDeleteResponse.from(customer);
    }
}
