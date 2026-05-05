package com.example.coffeeorderproject.global.common;

public record BaseErrorResponse (
        int status,
        String code,
        String errorMessage
) {}
