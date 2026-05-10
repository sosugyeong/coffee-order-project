package com.example.coffeeorderproject.domain.order.controller;

import com.example.coffeeorderproject.domain.order.dto.request.OrderRequest;
import com.example.coffeeorderproject.domain.order.dto.request.PaymentRequest;
import com.example.coffeeorderproject.domain.order.dto.response.OrderResponse;
import com.example.coffeeorderproject.domain.order.dto.response.PaymentResponse;
import com.example.coffeeorderproject.domain.order.service.OrderService;
import com.example.coffeeorderproject.global.annotation.Idempotent;
import com.example.coffeeorderproject.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cafe/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //주문 생성
    @Idempotent
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> orderCreate(
            @Valid @RequestBody OrderRequest request
    ){
        OrderResponse response = orderService.orderCreate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    //결제
    @Idempotent
    @PostMapping("/payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> paymentCreate(
            @RequestBody PaymentRequest request
    ) {
        PaymentResponse response = orderService.paymentCreate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }
}
