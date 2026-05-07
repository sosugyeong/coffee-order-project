package com.example.coffeeorderproject.domain.ranking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompletedEvent {
    private Long orderId;
    private Long menuId;
    private Long memberId;
    private Integer quantity;
    private Integer totalPrice;
    private String paidAt;
}
