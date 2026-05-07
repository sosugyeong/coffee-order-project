package com.example.coffeeorderproject.domain.ranking.producer;

import com.example.coffeeorderproject.domain.ranking.dto.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.example.coffeeorderproject.global.common.KafkaTopics.TOPIC_PAYMENT_COMPLETED;

@Service
@RequiredArgsConstructor
public class PaymentProducer {

    private final KafkaTemplate<String, PaymentCompletedEvent> paymentCompletedEventKafkaTemplate;

    public void send(PaymentCompletedEvent event){
        paymentCompletedEventKafkaTemplate.send(TOPIC_PAYMENT_COMPLETED, event);
    }
}
