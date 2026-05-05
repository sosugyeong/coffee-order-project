package com.example.coffeeorderproject.domain.point.dto.request;

public record PointRequest(
        String userIdentifier,
        Long point
) {
}
