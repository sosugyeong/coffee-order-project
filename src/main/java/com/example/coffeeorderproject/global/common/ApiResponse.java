package com.example.coffeeorderproject.global.common;

import com.example.coffeeorderproject.global.exception.ErrorCode;

public record ApiResponse<T> (
        boolean success,
        T data, //성공시 데이터
        BaseErrorResponse error //실패시 에러 정보
) {

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static ApiResponse<Void> fail(BaseErrorResponse errorResponse) {
        return new ApiResponse<>(false, null, errorResponse);
    }

    public static ApiResponse<Void> fail(ErrorCode errorCode) {
        return fail(new BaseErrorResponse(
                errorCode.getHttpStatus().value(),
                errorCode.getCode(),
                errorCode.getMessage()
        ));
    }

    public static ApiResponse<Void> fail(int status, String code, String message){
        return new ApiResponse<>(
                false, null, new BaseErrorResponse(status, code, message)
        );
    }
}
