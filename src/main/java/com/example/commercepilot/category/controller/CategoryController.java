package com.example.commercepilot.category.controller;

import com.example.commercepilot.admin.dto.session.LoginAdmin;
import com.example.commercepilot.category.dto.request.CategoryCreateRequest;
import com.example.commercepilot.category.dto.request.CategoryUpdateRequest;
import com.example.commercepilot.category.dto.response.CategoryCreateResponse;
import com.example.commercepilot.category.dto.response.CategoryDeleteResponse;
import com.example.commercepilot.category.dto.response.CategoryListResponse;
import com.example.commercepilot.category.dto.response.CategoryResponse;
import com.example.commercepilot.category.service.CategoryService;
import com.example.commercepilot.exception.ApiResponse;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.commercepilot.admin.config.SessionConst.LOGIN_ADMIN;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'OPERATION_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryCreateResponse>> createCategory(
            @SessionAttribute(name = LOGIN_ADMIN, required = false) LoginAdmin loginAdmin,
            @Valid @RequestBody CategoryCreateRequest request) {

        if (loginAdmin == null) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, categoryService.add(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CategoryListResponse>>> getCategories(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, categoryService.getCategories(pageable)));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategory(
            @PathVariable Long categoryId
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, categoryService.getCategory(categoryId)));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'OPERATION_ADMIN')")
    @GetMapping("/deleted")
    public ResponseEntity<ApiResponse<List<CategoryDeleteResponse>>> getDeletedCategories() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, categoryService.getDeletedCategories()));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'OPERATION_ADMIN')")
    @PutMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @SessionAttribute(name = LOGIN_ADMIN, required = false) LoginAdmin loginAdmin,
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryUpdateRequest request) {

        if (loginAdmin == null) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, categoryService.update(categoryId, request)));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'OPERATION_ADMIN')")
    @PutMapping("/{categoryId}/restore")
    public ResponseEntity<ApiResponse<Void>> restoreCategory(
            @SessionAttribute(name = LOGIN_ADMIN, required = false) LoginAdmin loginAdmin,
            @PathVariable Long categoryId) {

        if (loginAdmin == null) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        categoryService.restore(categoryId);
        return ResponseEntity.ok(ApiResponse.success(HttpStatus.OK));
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'OPERATION_ADMIN')")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryDeleteResponse>> deleteCategory(
            @SessionAttribute(name = LOGIN_ADMIN, required = false) LoginAdmin loginAdmin,
            @PathVariable Long categoryId) {

        if (loginAdmin == null) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, categoryService.delete(categoryId)));
    }
}
