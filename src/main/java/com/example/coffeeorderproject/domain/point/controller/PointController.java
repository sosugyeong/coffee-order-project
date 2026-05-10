package com.example.coffeeorderproject.domain.point.controller;

import com.example.coffeeorderproject.domain.point.dto.request.PointRequest;
import com.example.coffeeorderproject.domain.point.dto.response.PointResponse;
import com.example.coffeeorderproject.domain.point.service.PointService;
import com.example.coffeeorderproject.global.annotation.Idempotent;
import com.example.coffeeorderproject.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cafe/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @Idempotent(ttlSeconds = 5)
    @PostMapping("/charge")
    public ResponseEntity<ApiResponse<PointResponse>> chargePoint(
            @Valid @RequestBody PointRequest request
    ){
        PointResponse response = pointService.chargePoint(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }
}
