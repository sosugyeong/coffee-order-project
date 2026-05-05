package com.example.coffeeorderproject.domain.order.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {

    CREATED("주문 생성"),
    PAYMENT_WAITING("결제중"),
    COMPLETED("결제 완료");

    private final String description;
}
