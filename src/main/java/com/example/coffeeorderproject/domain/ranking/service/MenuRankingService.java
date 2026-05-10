package com.example.coffeeorderproject.domain.ranking.service;

import com.example.coffeeorderproject.domain.menu.repository.MenuRepository;
import com.example.coffeeorderproject.domain.ranking.dto.RankingDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuRankingService {

    public static final String MENU_RANKING_DAILY_KEY = "menu:ranking:";
    public static final String MENU_RANKING_TOP3_CACHE_KEY = "menu:ranking:top3:cache";

    private final MenuRepository menuRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;

    public void increaseMenuRanking(long menuId, LocalDate currentDate) {

        String key = MENU_RANKING_DAILY_KEY + currentDate.toString();
        stringRedisTemplate.opsForZSet().incrementScore(key, String.valueOf(menuId), 1);

        //TTL 확인
        Long expire = stringRedisTemplate.getExpire(key);
        if(expire != null && expire == -1L){
            redissonClient.getBucket(key).expire(7, TimeUnit.DAYS);
        }
    }

    public List<RankingDto> findMenuRankingTop3InToday() {
        String jsonResult = stringRedisTemplate.opsForValue().get(MENU_RANKING_TOP3_CACHE_KEY);

        if (jsonResult == null || jsonResult.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(jsonResult, new TypeReference<List<RankingDto>>() {});
        } catch (JsonProcessingException e) {
            log.error("[MenuRankingService] 랭킹 캐시 파싱 중 오류 발생", e);
            return Collections.emptyList();
        }
    }
}
