package com.example.coffeeorderproject.domain.menu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuCategory {
    COFFEE("커피"),
    DECAF("디카페인"),
    NON_COFFEE("논커피"),
    ADE("에이드/주스"),
    SMOOTHIE("스무디/프라페"),
    TEA("티"),
    FOOD("푸드");

    private final String description;
}
