package com.example.coffeeorderproject.domain.menu.dto.response;

import com.example.coffeeorderproject.domain.menu.entity.Menu;
import com.example.coffeeorderproject.domain.menu.enums.MenuCategory;
import com.example.coffeeorderproject.domain.menu.enums.MenuStatus;
import com.example.coffeeorderproject.domain.menu.enums.MenuTemperature;
import lombok.Builder;

@Builder
public record MenuResponse(
        Long id,
        String title,
        Integer price,
        MenuCategory category,
        MenuStatus status,
        MenuTemperature temp
) {
    public static MenuResponse from(Menu menu){
        return MenuResponse.builder()
                .id(menu.getId())
                .title(menu.getTitle())
                .price(menu.getPrice())
                .category(menu.getCategory())
                .status(menu.getStatus())
                .temp(menu.getTemp())
                .build();
    }
}
