package com.example.coffeeorderproject.domain.ranking.listener;

import com.example.coffeeorderproject.domain.ranking.dto.PaymentCompletedEvent;
import com.example.coffeeorderproject.domain.ranking.service.MenuRankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.example.coffeeorderproject.global.common.KafkaTopics.TOPIC_PAYMENT_COMPLETED;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuRankingListener {

    private final MenuRankingService menuRankingService;

    @KafkaListener(
            topics = TOPIC_PAYMENT_COMPLETED,
            groupId = "menu-ranking-group",
            containerFactory = "menuRankingKafkaListenerContainerFactory"
    )
    public void consume(PaymentCompletedEvent event){
        log.info("[MenuRanking] 결제 이벤트 수신: menuId={}, quantity={}",
                event.getMenuId(), event.getQuantity());

        LocalDate paidAt = LocalDate.parse(event.getPaidAt().substring(0, 10));

        //수량만큼 점수 증가
        for (int i = 0; i < event.getQuantity(); i++) {
            menuRankingService.increaseMenuRanking(event.getMenuId(), paidAt);
        }
    }
}
