package com.example.coffeeorderproject.domain.ranking.scheduler;

import com.example.coffeeorderproject.domain.menu.entity.Menu;
import com.example.coffeeorderproject.domain.menu.repository.MenuRepository;
import com.example.coffeeorderproject.domain.ranking.dto.RankingDto;
import com.example.coffeeorderproject.domain.ranking.service.MenuRankingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class MenuRankingScheduler {

    private final StringRedisTemplate stringRedisTemplate;
    private final MenuRepository menuRepository;
    private final ObjectMapper objectMapper;

    // 5분마다 실행
    @Scheduled(fixedRate = 300000)
    public void updateWeeklyRankingCache() {
        log.info("[MenuRankingScheduler] 주간 랭킹 캐시 갱신 시작");
        LocalDate today = LocalDate.now();
        String[] dailyKeys = new String[7];
        for (int i = 0; i < 7; i++) {
            dailyKeys[i] = MenuRankingService.MENU_RANKING_DAILY_KEY + today.minusDays(i).toString();
        }

        String weeklyKey = "menu:ranking:weekly:temp";

        try {
            //7일치 일별 데이터를 임시 키에 합산
            stringRedisTemplate.opsForZSet().unionAndStore(dailyKeys[0],
                    List.of(Arrays.copyOfRange(dailyKeys, 1, dailyKeys.length)), weeklyKey);

            // TOP3 조회
            Set<ZSetOperations.TypedTuple<String>> result = stringRedisTemplate.opsForZSet()
                    .reverseRangeWithScores(weeklyKey, 0, 2);

            List<RankingDto> top3Ranking;
            if (result == null || result.isEmpty()) {
                top3Ranking = Collections.emptyList();
            } else {
                List<Long> menuIds = result.stream().map(tuple -> Long.valueOf(tuple.getValue())).toList();

                Map<Long, String> menuTitleMap = menuRepository.findAllById(menuIds).stream()
                        .collect(Collectors.toMap(Menu::getId, Menu::getTitle));

                top3Ranking = result.stream()
                        .map(tuple -> {
                            Long id = Long.valueOf(tuple.getValue());
                            String title = menuTitleMap.getOrDefault(id, "알 수 없는 메뉴");
                            return new RankingDto(title, tuple.getScore());
                        })
                        .toList();
            }

            // DTO 리스트를 JSON으로 변환하여 메인 캐시 키에 저장 (TTL 10분)
            String cacheKey = MenuRankingService.MENU_RANKING_TOP3_CACHE_KEY;
            String jsonResult = objectMapper.writeValueAsString(top3Ranking);
            stringRedisTemplate.opsForValue().set(cacheKey, jsonResult, 10, TimeUnit.MINUTES);
            
            // 임시 키 삭제
            stringRedisTemplate.delete(weeklyKey);

            log.info("[MenuRankingScheduler] 주간 랭킹 캐시 갱신 완료. Top3 갯수: {}", top3Ranking.size());

        } catch (Exception e) {
            log.error("[MenuRankingScheduler] 랭킹 캐시 갱신 중 오류 발생", e);
        }
    }
}
