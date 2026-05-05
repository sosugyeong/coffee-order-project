package com.example.coffeeorderproject.domain.menu.dto.response;

import com.example.coffeeorderproject.domain.menu.enums.MenuCategory;
import com.example.coffeeorderproject.domain.menu.enums.MenuStatus;

public record MenuSearchCondition(
        MenuCategory category,
        MenuStatus status
) {
}
