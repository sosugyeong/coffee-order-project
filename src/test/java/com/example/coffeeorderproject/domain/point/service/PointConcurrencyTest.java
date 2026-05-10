package com.example.coffeeorderproject.domain.point.service;

import com.example.coffeeorderproject.domain.member.entity.Member;
import com.example.coffeeorderproject.domain.member.repository.MemberRepository;
import com.example.coffeeorderproject.domain.menu.entity.Menu;
import com.example.coffeeorderproject.domain.menu.enums.MenuCategory;
import com.example.coffeeorderproject.domain.menu.enums.MenuStatus;
import com.example.coffeeorderproject.domain.menu.enums.MenuTemperature;
import com.example.coffeeorderproject.domain.menu.repository.MenuRepository;
import com.example.coffeeorderproject.domain.order.dto.request.PaymentRequest;
import com.example.coffeeorderproject.domain.order.entity.Order;
import com.example.coffeeorderproject.domain.order.entity.OrderItem;
import com.example.coffeeorderproject.domain.order.enums.OrderStatus;
import com.example.coffeeorderproject.domain.order.repository.OrderItemRepository;
import com.example.coffeeorderproject.domain.order.repository.OrderRepository;
import com.example.coffeeorderproject.domain.order.service.OrderService;
import com.example.coffeeorderproject.domain.point.dto.request.PointRequest;
import com.example.coffeeorderproject.domain.point.enums.PointType;
import com.example.coffeeorderproject.domain.point.repository.PointRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointConcurrencyTest {

    @Autowired
    private PointService pointService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private PointRepository pointRepository;

    @AfterEach
    void tearDown() {
        orderItemRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        pointRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("한 사용자가 동시에 여러번 결제를 요청하면 1번만 성공하고 나머지는 실패해야 한다.")
    void paymentDoubleClickedConcurrencyTest() throws InterruptedException {
        // given
        Member member = memberRepository.save(Member.builder()
                .userIdentifier("test-user")
                .name("테스트유저1")
                .phoneNumber("010-0000-0000")
                .amount(10000)
                .build());

        Menu menu = menuRepository.save(Menu.builder()
                .title("커피")
                .price(10000)
                .category(MenuCategory.coffee)
                .status(MenuStatus.SALE)
                .temp(MenuTemperature.ICED)
                .build());

        // 결제 대기 중인 주문 생성
        Order order = Order.builder()
                .member(member)
                .status(OrderStatus.CREATED)
                .orderItems(new ArrayList<>())
                .build();
        
        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .menu(menu)
                .orderPrice(10000)
                .count(1)
                .build();
        order.getOrderItems().add(orderItem);
        order.calculateTotalAmount(); // 10,000원 세팅
        orderRepository.save(order);

        // 5개의 스레드에서 동일한 결제 요청
        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        PaymentRequest paymentRequest = new PaymentRequest(member.getUserIdentifier(), order.getId());

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderService.paymentCreate(paymentRequest);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        // 잔액 부족 또는 중복 결제 방지
        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failCount.get()).isEqualTo(4);

        // 최종 잔액이 0원인지 확인
        Member updatedMember = memberRepository.findByUserIdentifier("test-user").orElseThrow();
        assertThat(updatedMember.getAmount()).isEqualTo(0);
        
        // 주문 상태가 COMPLETED인지 확인
        Order updatedOrder = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    @DisplayName("서로 다른 100명의 사용자가 동시에 포인트를 충전하면 모두 정상 처리되어야 한다.")
    void multiUserConcurrentChargeTest() throws InterruptedException {
        // given
        int userCount = 100;
        List<String> userIdentifiers = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            String identifier = "user-" + i;
            userIdentifiers.add(identifier);
            memberRepository.save(Member.builder()
                    .userIdentifier(identifier)
                    .name("유저" + i)
                    .phoneNumber("010-1000-" + String.format("%04d", i))
                    .amount(0)
                    .build());
        }

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(userCount);

        // when
        for (int i = 0; i < userCount; i++) {
            final String identifier = userIdentifiers.get(i);
            executorService.submit(() -> {
                try {
                    PointRequest request = new PointRequest(identifier, 1000, PointType.CHARGE);
                    pointService.chargePoint(request);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        // 100명 모두 1000원씩 충전되어야 함
        for (String identifier : userIdentifiers) {
            Member member = memberRepository.findByUserIdentifier(identifier).orElseThrow();
            assertThat(member.getAmount()).isEqualTo(1000);
        }
    }
}
