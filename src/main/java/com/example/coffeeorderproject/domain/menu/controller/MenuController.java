package com.example.coffeeorderproject.domain.menu.controller;

import com.example.coffeeorderproject.domain.menu.dto.response.MenuResponse;
import com.example.coffeeorderproject.domain.menu.dto.response.MenuSearchCondition;
import com.example.coffeeorderproject.domain.menu.service.MenuService;
import com.example.coffeeorderproject.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cafe/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    //커피 메뉴 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<Page<MenuResponse>>> getMenuList(
            MenuSearchCondition cond,
            @PageableDefault(size = 20) Pageable pageable
    ){
        return ResponseEntity.ok(ApiResponse.success(menuService.getMenuList(cond, pageable)));
    }
}
