package com.example.commercepilot.admin.controller;

import com.example.commercepilot.admin.dto.request.AdminLoginRequest;
import com.example.commercepilot.admin.dto.request.AdminPasswordChangeRequest;
import com.example.commercepilot.admin.dto.request.AdminSignupRequest;
import com.example.commercepilot.admin.dto.response.AdminSignupResponse;
import com.example.commercepilot.admin.service.AdminAuthService;
import com.example.commercepilot.admin.service.AdminCommandService;
import com.example.commercepilot.exception.ApiResponse;
import com.example.commercepilot.web.AdminUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")
public class AdminAuthController {

    private final AdminCommandService adminCommandService;
    private final AdminAuthService adminAuthService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AdminSignupResponse>> signup(@Valid @RequestBody AdminSignupRequest request) {
        AdminSignupResponse response = adminCommandService.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody AdminLoginRequest request) {
        String token = adminAuthService.login(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, token));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
//        adminAuthService.logout(session);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'OPERATION_ADMIN', 'CS_ADMIN')")
    @PatchMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody AdminPasswordChangeRequest request,
            @AuthenticationPrincipal AdminUserDetails adminUserDetails
//            @SessionAttribute(name = SessionConst.LOGIN_ADMIN, required = false) LoginAdmin loginAdmin
    ) {
//        if (loginAdmin == null) {
//            throw new CustomException(ErrorCode.UNAUTHORIZED);
//        }

//        adminCommandService.changePassword(loginAdmin.adminId(), request);
        adminCommandService.changePassword(adminUserDetails.getId(), request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK));
    }
}