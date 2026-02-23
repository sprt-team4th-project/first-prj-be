package com.example.commercepilot.admin.service;

import com.example.commercepilot.admin.dto.request.AdminSearchCondition;
import com.example.commercepilot.admin.dto.response.AdminDetailResponse;
import com.example.commercepilot.admin.dto.response.AdminListResponse;
import com.example.commercepilot.admin.entity.Admin;
import com.example.commercepilot.admin.repository.AdminRepository;
import com.example.commercepilot.admin.repository.AdminSpecifications;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminQueryService {

    private final AdminRepository adminRepository;

    @Transactional(readOnly = true)
    public Page<AdminListResponse> search(AdminSearchCondition condition, Pageable pageable) {

        Specification<Admin> spec = Specification.where(AdminSpecifications.keyword(condition.keyword()))
                .and(AdminSpecifications.role(condition.role()))
                .and(AdminSpecifications.status(condition.status()));

        return adminRepository.findAll(spec, pageable).map(AdminListResponse::from);
    }

    @Transactional(readOnly = true)
    public AdminDetailResponse getDetail(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));
        return AdminDetailResponse.from(admin);
    }
}