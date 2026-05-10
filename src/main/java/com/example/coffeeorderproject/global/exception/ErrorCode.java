package com.example.coffeeorderproject.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ===== SERVER (S) =====
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"S001","서버 내부 오류가 발생했습니다."),

    // ===== MEMBER (M1) =====
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M101", "존재하지 않는 유저입니다."),

    // ===== MENU (M) =====
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "존재하지 않는 메뉴입니다."),

    // ===== POINT (P) =====
    INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "P001", "포인트 잔액이 충분하지 않습니다."),

    // ===== ORDER (O) =====
    ORDER_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "O001", "이미 완료된 주문입니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "O002", "존재하지 않는 주문 정보입니다."),
    UNAUTHORIZED_PAYMENT(HttpStatus.FORBIDDEN, "O003", "주문자 정보가 일치하지 않습니다."),

    // ===== GLOBAL/COMMON (G) =====
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "G001", "입력값이 올바르지 않습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "G002", "접근 권한이 없습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G003", "서버 오류가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "G004", "요청한 리소스를 찾을 수 없습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "G005", "잘못된 입력값입니다."),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "O002", "재고가 부족합니다."),
    DUPLICATE_REQUEST(HttpStatus.CONFLICT, "I001", "이미 처리 중이거나 완료된 요청입니다."),

    // ===== LOCK (L) =====
    LOCK_ACQUISITION_FAILED(HttpStatus.CONFLICT, "L001", "요청이 너무 많습니다. 잠시 후 다시 시도해주세요.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
