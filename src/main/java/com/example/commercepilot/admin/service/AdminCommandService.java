package com.example.commercepilot.admin.service;

import com.example.commercepilot.admin.dto.request.AdminPasswordChangeRequest;
import com.example.commercepilot.admin.dto.request.AdminSignupRequest;
import com.example.commercepilot.admin.dto.request.AdminUpdateRequest;
import com.example.commercepilot.admin.dto.response.AdminSignupResponse;
import com.example.commercepilot.admin.entity.Admin;
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

        // NOTE: PENDING이 아닌 경우에 대한 전용 ErrorCode가 있으면 더 적절함 (예: ADMIN_NOT_PENDING)
        if (admin.getStatus() != AdminStatus.PENDING) {
            throw new CustomException(ErrorCode.ADMIN_LOGIN_NOT_ACTIVE);
        }

        admin.changeStatus(AdminStatus.ACTIVE);
    }

    @Transactional
    public void reject(Long adminId) {

        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        // NOTE: PENDING이 아닌 경우에 대한 전용 ErrorCode가 있으면 더 적절함 (예: ADMIN_NOT_PENDING)
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

        // 1) 현재 비밀번호 검증
        if (!passwordEncoder.matches(request.currentPassword(), admin.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 2) 새 비밀번호 확인값 검증
        if (!request.newPassword().equals(request.newPasswordConfirm())) {
            throw new CustomException(ErrorCode.NEW_PASSWORD_CONFIRM_MISMATCH);
        }

        // 3) 기존 비밀번호와 동일한 새 비밀번호 방지
        if (passwordEncoder.matches(request.newPassword(), admin.getPassword())) {
            throw new CustomException(ErrorCode.ALREADY_USED_PASSWORD);
        }

        String encoded = passwordEncoder.encode(request.newPassword());
        admin.changePassword(encoded);
    }
}