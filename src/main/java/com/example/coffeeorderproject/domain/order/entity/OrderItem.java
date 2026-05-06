package com.example.coffeeorderproject.domain.order.entity;

import com.example.coffeeorderproject.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Table(name = "order_items")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Integer orderPrice; // 주문 시점의 가격
    private Integer count; // 주문 수량

    //메뉴 금액
    public int getTotalPrice() {
        return this.orderPrice * this.count;
    }
}
