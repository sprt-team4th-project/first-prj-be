package com.example.commercepilot.admin.dto;

import com.example.commercepilot.admin.entity.AdminRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AdminSignupRequest(

        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @Email(message = "이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일은 필수입니다.")
        String email,

        @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,

        @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식은 010-XXXX-XXXX 입니다.")
        @NotBlank(message = "전화번호는 필수입니다.")
        String callNumber,

        @NotNull(message = "역할은 필수입니다.")
        AdminRole role
) {}