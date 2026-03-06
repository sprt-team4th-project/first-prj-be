package com.example.commercepilot.admin.service;

import com.example.commercepilot.admin.dto.request.AdminLoginRequest;
import com.example.commercepilot.admin.entity.Admin;
import com.example.commercepilot.admin.entity.AdminStatus;
import com.example.commercepilot.admin.repository.AdminRepository;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import com.example.commercepilot.web.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public String login(AdminLoginRequest request) {

        Admin admin = adminRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        // 비밀번호 불일치
        if (!passwordEncoder.matches(request.password(), admin.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        // 상태별 로그인 차단
        validateLoginStatus(admin.getStatus());

        return jwtUtil.generateToken(admin.getId(), admin.getEmail(), admin.getRole());

//        // 세션 저장 (필수: categoryId/email/role)
//        LoginAdmin loginAdmin = new LoginAdmin(admin.getId(), admin.getEmail(), admin.getRole());
//        session.setAttribute(SessionConst.LOGIN_ADMIN, loginAdmin);
//
//        // 세션 유효시간은 application.properties (24h)로 설정됨
    }

//    public void logout(HttpSession session) {
//        session.invalidate();
//    }

    private void validateLoginStatus(AdminStatus status) {
        if (status == null) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        switch (status) {
            case ACTIVE -> { /* ok */ }
            case PENDING -> throw new CustomException(ErrorCode.ADMIN_PENDING);
            case REJECTED -> throw new CustomException(ErrorCode.ADMIN_REJECTED);
            case SUSPENDED -> throw new CustomException(ErrorCode.ADMIN_SUSPENDED);
            case INACTIVE -> throw new CustomException(ErrorCode.ADMIN_INACTIVE);
            default -> throw new CustomException(ErrorCode.ADMIN_LOGIN_NOT_ACTIVE);
        }
    }
}