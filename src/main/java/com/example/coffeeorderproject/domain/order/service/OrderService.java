package com.example.coffeeorderproject.domain.order.service;

import com.example.coffeeorderproject.domain.member.entity.Member;
import com.example.coffeeorderproject.domain.member.repository.MemberRepository;
import com.example.coffeeorderproject.domain.menu.entity.Menu;
import com.example.coffeeorderproject.domain.menu.repository.MenuRepository;
import com.example.coffeeorderproject.domain.order.dto.request.OrderRequest;
import com.example.coffeeorderproject.domain.order.dto.request.PaymentRequest;
import com.example.coffeeorderproject.domain.order.dto.response.OrderResponse;
import com.example.coffeeorderproject.domain.order.dto.response.PaymentResponse;
import com.example.coffeeorderproject.domain.order.entity.Order;
import com.example.coffeeorderproject.domain.order.entity.OrderItem;
import com.example.coffeeorderproject.domain.order.enums.OrderStatus;
import com.example.coffeeorderproject.domain.order.repository.OrderRepository;
import com.example.coffeeorderproject.domain.ranking.dto.PaymentCompletedEvent;
import com.example.coffeeorderproject.domain.ranking.producer.PaymentProducer;
import com.example.coffeeorderproject.global.exception.BusinessException;
import com.example.coffeeorderproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    private final PaymentProducer paymentProducer;

    @Transactional
    public OrderResponse orderCreate(OrderRequest request){
        Member member = memberRepository.findByUserIdentifier(request.userIdentifier())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<Long> menuIds = request.items().stream()
                .map(OrderRequest.OrderItemRequest::menuId).toList();
        List<Menu> menus = menuRepository.findAllById(menuIds);

        Order order = Order.builder()
                .member(member)
                .status(OrderStatus.CREATED)
                .orderItems(new ArrayList<>())
                .build();

        for(OrderRequest.OrderItemRequest itemRequest : request.items()){
            Menu menu = menus.stream()
                    .filter(m -> m.getId().equals(itemRequest.menuId()))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(ErrorCode.MENU_NOT_FOUND));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .menu(menu)
                    .orderPrice(menu.getPrice())
                    .count(itemRequest.count())
                    .build();

            order.getOrderItems().add(orderItem);
        }

        //총 금액 계산
        order.calculateTotalAmount();

        Order savedOrder = orderRepository.save(order);
        sendToDataPlatform(order);

        return OrderResponse.from(savedOrder);
    }

    @Transactional
    public PaymentResponse paymentCreate(PaymentRequest request){
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        Member member = memberRepository.findByUserIdentifier(request.userIdentifier())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if(!order.getMember().getId().equals(member.getId())){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_PAYMENT);
        }
        if (order.getStatus() == OrderStatus.COMPLETED){
            throw new BusinessException(ErrorCode.ORDER_ALREADY_COMPLETED);
        }

        //포인트 차감, 주문 상태 변경
        member.usedPoint(order.getTotalPrice());
        order.updateStatus(OrderStatus.COMPLETED);

        for(OrderItem item : order.getOrderItems()) {
            PaymentCompletedEvent event = PaymentCompletedEvent.builder()
                    .orderId(order.getId())
                    .menuId(item.getMenu().getId())
                    .memberId(member.getId())
                    .quantity(item.getCount())
                    .totalPrice(item.getOrderPrice() * item.getCount())
                    .paidAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();
            paymentProducer.send(event);
        }

        sendToDataPlatform(order);
        return PaymentResponse.from(order, member);
    }

    private void sendToDataPlatform(Order order){
        System.out.println("[Data Platform Send] " +
                "User: " + order.getMember().getUserIdentifier() +
                ", TotalAmount: " + order.getTotalPrice());
    }
}
