package com.example.coffeeorderproject.domain.order.dto.response;

import com.example.coffeeorderproject.domain.order.entity.Order;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderResponse(
        Long orderId,
        String userName,
        Integer totalPayAmount,
        List<OrderItemDto> orderItems
) {
    public record OrderItemDto(
            String menuTitle,
            Integer count,
            Integer orderPrice
    ) {}

    public static OrderResponse from(Order order){
        return OrderResponse.builder()
                .orderId(order.getId())
                .userName(order.getMember().getName())
                .totalPayAmount(order.getTotalPrice())
                .orderItems(order.getOrderItems().stream()
                        .map(item -> new OrderItemDto(
                                item.getMenu().getTitle(),
                                item.getCount(),
                                item.getOrderPrice()))
                        .toList())
                .build();
    }
}
