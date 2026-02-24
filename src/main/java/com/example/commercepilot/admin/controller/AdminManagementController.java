package com.example.commercepilot.admin.controller;

import com.example.commercepilot.admin.config.SessionConst;
import com.example.commercepilot.admin.dto.request.AdminModifyRequest;
import com.example.commercepilot.admin.dto.request.AdminRoleChangeRequest;
import com.example.commercepilot.admin.dto.request.AdminSearchCondition;
import com.example.commercepilot.admin.dto.request.AdminStatusChangeRequest;
import com.example.commercepilot.admin.dto.response.AdminDetailResponse;
import com.example.commercepilot.admin.dto.response.AdminListResponse;
import com.example.commercepilot.admin.dto.session.LoginAdmin;
import com.example.commercepilot.admin.entity.AdminRole;
import com.example.commercepilot.admin.entity.AdminStatus;
import com.example.commercepilot.admin.service.AdminCommandService;
import com.example.commercepilot.admin.service.AdminQueryService;
import com.example.commercepilot.exception.ApiResponse;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admins")

public class AdminManagementController {

    private final AdminQueryService adminQueryService;
    private final AdminCommandService adminCommandService;

    @GetMapping
    public ApiResponse<Page<AdminListResponse>> searchAdmins(
            @ModelAttribute AdminSearchCondition condition,
            Pageable pageable,
            HttpSession session
    ) {
        requireSuperAdmin(session);
        return ApiResponse.success(HttpStatus.OK, adminQueryService.search(condition, pageable));
    }

    @GetMapping("/{adminId}")
    public ApiResponse<AdminDetailResponse> getAdminDetail(
            @PathVariable Long adminId,
            HttpSession session
    ) {
        requireSuperAdmin(session);
        return ApiResponse.success(HttpStatus.OK, adminQueryService.getDetail(adminId));
    }

    private void requireSuperAdmin(HttpSession session) {
        // TODO: SuperAdmin 세션 로그인 구현되면 아래 키로 체크 가능
        Object superAdmin = session.getAttribute(SessionConst.LOGIN_SUPER_ADMIN);
        if (superAdmin != null) return;

        LoginAdmin loginAdmin = (LoginAdmin) session.getAttribute(SessionConst.LOGIN_ADMIN);
        if (loginAdmin == null) throw new CustomException(ErrorCode.UNAUTHORIZED);
        if (loginAdmin.role() != AdminRole.SUPER_ADMIN) throw new CustomException(ErrorCode.ACCESS_DENIED);
    }

    @PatchMapping("/{adminId}")
    public ApiResponse<Void> updateAdmin(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminModifyRequest request,
            HttpSession session
    ) {
        requireSuperAdmin(session);
        adminCommandService.updateAdmin(adminId, request);
        return ApiResponse.success(HttpStatus.OK);
    }

    @PatchMapping("/{adminId}/role")
    public ApiResponse<Void> changeRole(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminRoleChangeRequest request,
            HttpSession session
    ) {
        requireSuperAdmin(session);
        adminCommandService.changeRole(adminId, request.role());
        return ApiResponse.success(HttpStatus.OK);
    }

    @PatchMapping("/{adminId}/status")
    public ApiResponse<Void> changeStatus(
            @PathVariable Long adminId,
            @Valid @RequestBody AdminStatusChangeRequest request,
            HttpSession session
    ) {
        requireSuperAdmin(session);
        adminCommandService.changeStatus(adminId, request.status());
        return ApiResponse.success(HttpStatus.OK);
    }

    @DeleteMapping("/{adminId}")
    public ApiResponse<Void> deleteAdmin(
            @PathVariable Long adminId,
            HttpSession session
    ) {
        requireSuperAdmin(session);
        adminCommandService.deleteAdmin(adminId);
        return ApiResponse.success(HttpStatus.OK);
    }
}