package com.example.commercepilot.admin.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdminRole {
    SUPER_ADMIN("ROLE_SUPER_ADMIN", "슈퍼관리자 권한"),
    OPERATION_ADMIN("ROLE_OPERATION_ADMIN", "운영관리자 권한"),
    CS_ADMIN("ROLE_CS_ADMIN", "CS 관리자 권한");

    private final String role;
    private final String description;
}
