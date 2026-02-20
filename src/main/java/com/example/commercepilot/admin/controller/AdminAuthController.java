package com.example.commercepilot.admin.controller;

import com.example.commercepilot.admin.dto.request.AdminLoginRequest;
import com.example.commercepilot.admin.dto.request.AdminSignupRequest;
import com.example.commercepilot.admin.dto.response.AdminSignupResponse;
import com.example.commercepilot.admin.service.AdminAuthService;
import com.example.commercepilot.admin.service.AdminCommandService;
import com.example.commercepilot.exception.ApiResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admins")
public class AdminAuthController {

    private final AdminCommandService adminCommandService;
    private final AdminAuthService adminAuthService;

    @PostMapping("/signup")
    public ApiResponse<AdminSignupResponse> signup(@Valid @RequestBody AdminSignupRequest request) {
        AdminSignupResponse response = adminCommandService.signup(request);
        return ApiResponse.success(HttpStatus.CREATED, response);
    }

    @PostMapping("/login")
    public ApiResponse<Void> login(@Valid @RequestBody AdminLoginRequest request, HttpSession session) {
        adminAuthService.login(request, session);
        return ApiResponse.success(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpSession session) {
        adminAuthService.logout(session);
        return ApiResponse.success(HttpStatus.OK);
    }
}