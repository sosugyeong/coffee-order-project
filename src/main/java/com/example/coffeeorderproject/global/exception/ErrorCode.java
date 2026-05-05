package com.example.coffeeorderproject.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ===== SERVER (S) =====
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"S001","서버 내부 오류가 발생했습니다."),


    // ===== MENU (M) =====
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "존재하지 않는 메뉴입니다."),

    // ===== POINT (P) =====
    INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "P001", "포인트 잔액이 충분하지 않습니다."),

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
