package com.example.coffeeorderproject.domain.menu.repository;

import com.example.coffeeorderproject.domain.menu.entity.Menu;
import com.example.coffeeorderproject.domain.menu.enums.MenuCategory;
import com.example.coffeeorderproject.domain.menu.enums.MenuStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    Page<Menu> findAllByCategoryAndStatus(MenuCategory category, MenuStatus status, Pageable pageable);
}
