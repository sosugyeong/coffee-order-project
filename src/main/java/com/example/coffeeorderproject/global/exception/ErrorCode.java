package com.example.coffeeorderproject.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ===== SERVER (S) =====
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"S001","서버 내부 오류가 발생했습니다."),

    // ===== AUTH (A) =====
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "A001", "이미 사용 중인 이메일입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A002", "이메일 또는 비밀번호가 올바르지 않습니다."),

    // ===== MEMBER (M) =====
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 회원입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "M002", "현재 비밀번호가 올바르지 않습니다."),
    SAME_AS_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, "M003", "새 비밀번호는 현재 비밀번호와 달라야 합니다."),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, "M004", "이미 사용 중인 닉네임입니다."),

    // ===== CATEGORY (C) =====
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "존재하지 않는 카테고리입니다."),

    // ===== PRODUCT (P) =====
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "존재하지 않는 상품입니다."),

    // ===== GLOBAL/COMMON (G) =====
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "G001", "입력값이 올바르지 않습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "G002", "접근 권한이 없습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G003", "서버 오류가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "G004", "요청한 리소스를 찾을 수 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "G005", "잘못된 입력값입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
