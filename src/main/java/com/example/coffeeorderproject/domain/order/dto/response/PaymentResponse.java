package com.example.coffeeorderproject.domain.order.dto.response;

import com.example.coffeeorderproject.domain.member.entity.Member;
import com.example.coffeeorderproject.domain.order.entity.Order;
import lombok.Builder;

import java.util.List;

@Builder
public record PaymentResponse(
        Long orderId,
        Integer totalPrice, //결제할 총 금액
        Integer remainPoint, //결제 후 남은 포인트
        List<PaymentItemDto> items
) {
    public record PaymentItemDto(
            String menuTitle,
            Integer count
    ) {}

    public static PaymentResponse from(Order order, Member member){
        return PaymentResponse.builder()
                .orderId(order.getId())
                .totalPrice(order.getTotalPrice())
                .remainPoint(member.getAmount())
                .items(order.getOrderItems().stream()
                        .map(item -> new PaymentItemDto(
                                item.getMenu().getTitle(),
                                item.getCount()))
                        .toList())
                .build();
    }
}
