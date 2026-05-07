package com.example.coffeeorderproject.domain.ranking.controller;

import com.example.coffeeorderproject.domain.ranking.dto.RankingDto;
import com.example.coffeeorderproject.domain.ranking.service.MenuRankingService;
import com.example.coffeeorderproject.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cafe/menu/ranking")
@RequiredArgsConstructor
public class MenuRankingController {

    private final MenuRankingService menuRankingService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RankingDto>>> findMenuRankingTop3InToday(){
        List<RankingDto> dtos = menuRankingService.findMenuRankingTop3InToday();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(dtos));
    }
}
