package com.example.coffeeorderproject.domain.menu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuStatus {
    SALE("판매중"),
    SOLD("품절");

    private final String description;
}
