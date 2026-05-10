package com.example.coffeeorderproject.domain.order.manager;

import com.example.coffeeorderproject.domain.member.entity.Member;
import com.example.coffeeorderproject.domain.member.repository.MemberRepository;
import com.example.coffeeorderproject.domain.order.dto.request.PaymentRequest;
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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class OrderPaymentManager {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final PaymentProducer paymentProducer;

    @Transactional
    public PaymentResponse executePayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
        Member member = memberRepository.findByUserIdentifier(request.userIdentifier())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (!order.getMember().getId().equals(member.getId())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_PAYMENT);
        }
        if (order.getStatus() == OrderStatus.COMPLETED) {
            throw new BusinessException(ErrorCode.ORDER_ALREADY_COMPLETED);
        }

        // 포인트 차감, 주문 상태 변경
        member.usedPoint(order.getTotalPrice());
        order.updateStatus(OrderStatus.COMPLETED);

        for (OrderItem item : order.getOrderItems()) {
            PaymentCompletedEvent event = PaymentCompletedEvent.builder()
                    .orderId(order.getId())
                    .menuId(item.getMenu().getId())
                    .memberId(member.getId())
                    .menuTitle(item.getMenu().getTitle())
                    .quantity(item.getCount())
                    .totalPrice(item.getOrderPrice() * item.getCount())
                    .paidAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .build();
            paymentProducer.send(event);
        }

        sendToDataPlatform(order);
        return PaymentResponse.from(order, member);
    }

    private void sendToDataPlatform(Order order) {
        System.out.println("[Data Platform Send] " +
                "User: " + order.getMember().getUserIdentifier() +
                ", TotalAmount: " + order.getTotalPrice());
    }
}
