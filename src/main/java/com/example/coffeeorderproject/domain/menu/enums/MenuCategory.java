package com.example.coffeeorderproject.domain.menu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuCategory {
    coffee("커피"),
    decaf("디카페인"),
    non_coffee("논커피"),
    ade("에이드/주스"),
    smoothie("스무디/프라페"),
    tea("티"),
    food("푸드");

    private final String description;
}
