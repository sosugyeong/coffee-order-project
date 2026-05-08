package com.example.coffeeorderproject.domain.order.listener;

import com.example.coffeeorderproject.domain.order.service.OrderService;
import com.example.coffeeorderproject.domain.ranking.dto.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.example.coffeeorderproject.global.common.KafkaTopics.TOPIC_PAYMENT_COMPLETED;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataPlatformMockListener {

    @KafkaListener(
            topics = TOPIC_PAYMENT_COMPLETED,
            groupId = "data-platform-group",
            containerFactory = "menuRankingKafkaListenerContainerFactory"
    )
    public void consume(PaymentCompletedEvent event){
        log.info("[DataPlatform] 수집된 주문 데이터: 주문번호={}, 메뉴={}, 금액={}",
                event.getOrderId(), event.getMenuTitle(), event.getTotalPrice());
    }
}
