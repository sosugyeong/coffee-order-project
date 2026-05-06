package com.example.coffeeorderproject.domain.point.dto.request;

import com.example.coffeeorderproject.domain.point.enums.PointType;

public record PointRequest(
        String userIdentifier,
        Integer point,
        PointType type
) {
}
