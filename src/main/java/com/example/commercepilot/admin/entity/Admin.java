package com.example.commercepilot.admin.entity;

import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "admin_name", nullable = false, length = 50)
    private String adminName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "call_number", nullable = false, length = 20)
    private String callNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AdminRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AdminStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // ERD에서 admin 테이블에 superAdmin_id (FK) 있었으니 일단 ID만 들고감 (단방향 조건)
    @Column(name = "super_admin_id")
    private Long superAdminId;

    public static Admin pending(
            String adminName,
            String email,
            String encodedPassword,
            String callNumber,
            AdminRole role
    ) {
        Admin admin = new Admin();
        admin.adminName = adminName;
        admin.email = email;
        admin.password = encodedPassword;
        admin.callNumber = callNumber;
        admin.role = role;
        admin.status = AdminStatus.PENDING;
        return admin;
    }

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = this.createdAt;
    }

    @PreUpdate
    void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    public void changeStatus(AdminStatus status) {
        this.status = status;
    }

    public void updateProfile(String name, String email, String callNumber) {
        this.adminName = name;
        this.email = email;
        this.callNumber = callNumber;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    /**
     * 비밀번호 변경(검증 포함)을 Admin 도메인 책임으로 위임
     */
    public void changePasswordWithValidation(
            PasswordEncoder passwordEncoder,
            String currentPassword,
            String newPassword,
            String newPasswordConfirm
    ) {
        // 1) 현재 비밀번호 검증
        if (!passwordEncoder.matches(currentPassword, this.password)) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        // 2) 새 비밀번호 확인값 검증
        if (!newPassword.equals(newPasswordConfirm)) {
            throw new CustomException(ErrorCode.NEW_PASSWORD_CONFIRM_MISMATCH);
        }

        // 3) 기존 비밀번호와 동일한 새 비밀번호 방지
        if (passwordEncoder.matches(newPassword, this.password)) {
            throw new CustomException(ErrorCode.ALREADY_USED_PASSWORD);
        }

        String encoded = passwordEncoder.encode(newPassword);
        this.changePassword(encoded);
    }
    public void changeRole(AdminRole role) {
        this.role = role;
    }

    public void delete() {
        this.status = AdminStatus.DELETED;
    }
}