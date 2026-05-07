package com.example.coffeeorderproject.domain.order.dto.request;

public record PaymentRequest(
        String userIdentifier,
        Long orderId
) {
}
