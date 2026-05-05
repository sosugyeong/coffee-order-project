package com.example.coffeeorderproject.domain.menu.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuTemperature {
    ICED("아이스"),
    HOT("핫");

    private final String description;
}
