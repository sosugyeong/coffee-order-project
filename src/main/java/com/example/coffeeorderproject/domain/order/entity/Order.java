package com.example.coffeeorderproject.domain.order.entity;

import com.example.coffeeorderproject.domain.member.entity.Member;
import com.example.coffeeorderproject.domain.order.enums.OrderStatus;
import com.example.coffeeorderproject.global.common.BaseEntity;
import com.example.coffeeorderproject.global.exception.BusinessException;
import com.example.coffeeorderproject.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    //총 금액 계산
    public void calculateTotalAmount(){
        this.totalPrice = orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }

    //주문 상태 변경
    public void updateStatus(OrderStatus nextStatus){
        if(this.status == OrderStatus.COMPLETED){
            throw new BusinessException(ErrorCode.ORDER_ALREADY_COMPLETED);
        }
        //현재 상태를 입력받은 상태로 변경
        this.status = nextStatus;
    }
}
