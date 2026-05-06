package com.example.coffeeorderproject.domain.order.dto.request;

import java.util.List;

public record OrderRequest(
        String userIdentifier,
        List<OrderItemRequest> items
) {
    public record OrderItemRequest(
            Long menuId,
            Integer count
    ) {}
}
