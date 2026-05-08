package com.example.coffeeorderproject.domain.ranking.service;

import com.example.coffeeorderproject.domain.ranking.dto.RankingDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuRankingService {

    public static final String MENU_RANKING_DAILY_KEY = "menu:ranking:";

    private final StringRedisTemplate stringRedisTemplate;

    public void increaseMenuRanking(long menuId, LocalDate currentDate) {
        String key = MENU_RANKING_DAILY_KEY + currentDate.toString();
        stringRedisTemplate.opsForZSet().incrementScore(key, String.valueOf(menuId), 1);

        //TTL 확인
        Long expire = stringRedisTemplate.getExpire(key);
        if(expire != null && expire == -1L){
            stringRedisTemplate.expire(key, 7, TimeUnit.DAYS);
        }
    }

    public List<RankingDto> findMenuRankingTop3InToday() {

        LocalDate today = LocalDate.now();

        //최근 7일간의 일별 키 목록 생성
        String[] dailyKeys = new String[7];
        for (int i = 0; i < 7; i++) {
            dailyKeys[i] = MENU_RANKING_DAILY_KEY + today.minusDays(i).toString();
        }

        //7일치 합산 키
        String weeklyKey = "menu:ranking:weekly:" + today.toString();

        //ZUNIONSTORE: 7일치 일별 sorted set을 합산
        stringRedisTemplate.opsForZSet().unionAndStore(dailyKeys[0],
                List.of(Arrays.copyOfRange(dailyKeys, 1, dailyKeys.length)), weeklyKey);

        //합산 키에 짧은 TTL 설정
       stringRedisTemplate.expire(weeklyKey, 5, TimeUnit.MINUTES);

        //TOP3 조회
        Set<ZSetOperations.TypedTuple<String>> result = stringRedisTemplate.opsForZSet()
                .reverseRangeWithScores(weeklyKey, 0, 2);

        if (result == null) {
            return Collections.emptyList();
        }

        return result.stream()
                .map(tuple -> new RankingDto(tuple.getValue(), tuple.getScore()))
                .toList();
    }
}
