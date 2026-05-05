package com.example.coffeeorderproject.domain.point.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PointType {
    CHARGE("충전"),
    USED("사용");

    private final String description;
}
