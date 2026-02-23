package com.example.commercepilot.admin.service;

import com.example.commercepilot.admin.dto.request.AdminModifyRequest;
import com.example.commercepilot.admin.dto.request.AdminPasswordChangeRequest;
import com.example.commercepilot.admin.dto.request.AdminSignupRequest;
import com.example.commercepilot.admin.dto.request.AdminUpdateRequest;
import com.example.commercepilot.admin.dto.response.AdminSignupResponse;
import com.example.commercepilot.admin.entity.Admin;
import com.example.commercepilot.admin.entity.AdminRole;
import com.example.commercepilot.admin.entity.AdminStatus;
import com.example.commercepilot.admin.repository.AdminRepository;
import com.example.commercepilot.config.PasswordEncoder;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCommandService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AdminSignupResponse signup(AdminSignupRequest request) {

        if (adminRepository.existsByEmail(request.email())) {
            throw new CustomException(ErrorCode.ADMIN_EMAIL_DUPLICATED);
        }

        String encoded = passwordEncoder.encode(request.password());

        Admin admin = Admin.pending(
                request.name(),
                request.email(),
                encoded,
                request.callNumber(),
                request.role()
        );

        Admin saved = adminRepository.save(admin);
        return AdminSignupResponse.from(saved);
    }

    @Transactional
    public void approve(Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        if (admin.getStatus() != AdminStatus.PENDING) {
            throw new CustomException(ErrorCode.ADMIN_LOGIN_NOT_ACTIVE);
        }

        admin.changeStatus(AdminStatus.ACTIVE);
    }

    @Transactional
    public void reject(Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        if (admin.getStatus() != AdminStatus.PENDING) {
            throw new CustomException(ErrorCode.ADMIN_LOGIN_NOT_ACTIVE);
        }

        admin.changeStatus(AdminStatus.REJECTED);
    }

    public AdminSignupResponse getMyProfile(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        return AdminSignupResponse.from(admin);
    }

    @Transactional
    public void updateMyProfile(Long adminId, AdminUpdateRequest request) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        admin.updateProfile(request.name(), request.email(), request.callNumber());
    }

    @Transactional
    public void changePassword(Long adminId, AdminPasswordChangeRequest request) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        admin.changePasswordWithValidation(
                passwordEncoder,
                request.currentPassword(),
                request.newPassword(),
                request.newPasswordConfirm()
        );
    }

    @Transactional
    public void updateAdmin(Long adminId, AdminModifyRequest request) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        if (!admin.getEmail().equals(request.email())
                && adminRepository.existsByEmail(request.email())) {
            throw new CustomException(ErrorCode.ADMIN_EMAIL_DUPLICATED);
        }

        admin.updateProfile(request.name(), request.email(), request.callNumber());
    }

    @Transactional
    public void changeRole(Long adminId, AdminRole role) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        admin.changeRole(role);
    }

    @Transactional
    public void changeStatus(Long adminId, AdminStatus status) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        admin.changeStatus(status);
    }

    @Transactional
    public void deleteAdmin(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        admin.delete(); // status를 DELETED로 변경하는 소프트 삭제
    }
}