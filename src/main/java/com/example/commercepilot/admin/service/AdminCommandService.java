package com.example.commercepilot.admin.service;

import com.example.commercepilot.admin.dto.request.AdminSignupRequest;
import com.example.commercepilot.admin.dto.response.AdminSignupResponse;
import com.example.commercepilot.admin.entity.Admin;
import com.example.commercepilot.admin.repository.AdminRepository;
import com.example.commercepilot.config.PasswordEncoder;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
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
}