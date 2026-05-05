package com.example.coffeeorderproject.domain.order.repository;

import com.example.coffeeorderproject.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
