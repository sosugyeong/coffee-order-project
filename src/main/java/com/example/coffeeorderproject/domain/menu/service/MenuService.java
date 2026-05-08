package com.example.coffeeorderproject.domain.menu.service;

import com.example.coffeeorderproject.domain.menu.dto.response.MenuResponse;
import com.example.coffeeorderproject.domain.menu.dto.response.MenuSearchCondition;
import com.example.coffeeorderproject.domain.menu.entity.Menu;
import com.example.coffeeorderproject.domain.menu.enums.MenuCategory;
import com.example.coffeeorderproject.domain.menu.enums.MenuStatus;
import com.example.coffeeorderproject.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    //커피 메뉴 조회
    @Transactional(readOnly = true)
    public Page<MenuResponse> getMenuList(MenuSearchCondition cond, Pageable pageable){
        //필요시 기본 값 적용
        MenuSearchCondition searchCond = applyDefaultStatus(cond);

        //db 조회
        Page<Menu> menuPage = menuRepository.findAllByCategoryAndStatus(
                searchCond.category(),
                searchCond.status(),
                pageable
        );

        return menuPage.map(MenuResponse::from);
    }

    private MenuSearchCondition applyDefaultStatus(MenuSearchCondition cond){
        //미입력시 기본값 세팅
        if(cond.category() == null){
            return new MenuSearchCondition(
                    MenuCategory.COFFEE,
                    MenuStatus.SALE
            );
        }
        return cond;
    }
}
