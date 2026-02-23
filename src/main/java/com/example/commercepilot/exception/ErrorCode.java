package com.example.commercepilot.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

//    공통 관련 에러 코드("A###")
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "A001", "비밀번호가 일치하지 않습니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "A002", "입력값이 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A003", "로그인이 필요한 기능입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "A004", "권한이 없습니다."),
    ALREADY_USED_PASSWORD(HttpStatus.BAD_REQUEST, "A005", "이미 사용 중인 비밀번호입니다."),
    NEW_PASSWORD_CONFIRM_MISMATCH(HttpStatus.BAD_REQUEST, "A006", "새 비밀번호 확인이 일치하지 않습니다."),

//    일정 관련 에러 코드("S###")
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "해당 일정은 존재하지 않습니다."),

    // 관리자 관련 에러 코드("M###")
    ADMIN_EMAIL_DUPLICATED(HttpStatus.CONFLICT, "M001", "이미 사용 중인 이메일입니다."),
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "M002", "해당 관리자는 존재하지 않습니다."),
    ADMIN_LOGIN_NOT_ACTIVE(HttpStatus.FORBIDDEN, "M003", "활성 상태의 관리자만 로그인할 수 있습니다."),
    ADMIN_PENDING(HttpStatus.FORBIDDEN, "M004", "승인대기 상태입니다."),
    ADMIN_REJECTED(HttpStatus.FORBIDDEN, "M005", "가입 신청이 거부된 계정입니다."),
    ADMIN_SUSPENDED(HttpStatus.FORBIDDEN, "M006", "정지된 계정입니다."),
    ADMIN_INACTIVE(HttpStatus.FORBIDDEN, "M007", "비활성화된 계정입니다."),

    // 상품 관련 에러 코드("P###")
    PRODUCT_DISCONTINUED(HttpStatus.BAD_REQUEST, "P001", "단종된 상품입니다."),
    PRODUCT_SOLD_OUT(HttpStatus.BAD_REQUEST, "P002", "품절된 상품입니다."),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "P003", "재고가 부족합니다."),

    // 고객 관련 에러 코드("CU###")
    CUSTOMER_ID_REQUIRED(HttpStatus.BAD_REQUEST, "CU001", "고객 ID는 필수입니다."),
    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND, "CU002", "해당 고객을 찾을 수 없습니다."),

//    유저 관련 에러 코드("U###")
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "해당 유저는 존재하지 않습니다"),

//    댓글 관련 에러 코드("C###")
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "해당 댓글은 존재하지 않습니다."),
    COMMENT_NOT_IN_SCHEDULE(HttpStatus.NOT_FOUND, "C002", "해당 일정에 속한 댓글이 아닙니다."),

//    로그인 관련 에러 코드("L###")
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "L001", "이메일 또는 비밀번호가 일치하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}