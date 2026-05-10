package com.example.coffeeorderproject.domain.order.repository;

import com.example.coffeeorderproject.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
