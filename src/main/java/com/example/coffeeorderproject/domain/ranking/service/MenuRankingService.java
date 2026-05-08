package com.example.coffeeorderproject.domain.ranking.service;

import com.example.coffeeorderproject.domain.menu.entity.Menu;
import com.example.coffeeorderproject.domain.menu.repository.MenuRepository;
import com.example.coffeeorderproject.domain.ranking.dto.RankingDto;
import com.example.coffeeorderproject.global.exception.BusinessException;
import com.example.coffeeorderproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

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

    private final MenuRepository menuRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;

    public void increaseMenuRanking(long menuId, LocalDate currentDate) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new BusinessException(ErrorCode.MENU_NOT_FOUND));

        String key = MENU_RANKING_DAILY_KEY + currentDate.toString();
        stringRedisTemplate.opsForZSet().incrementScore(key, menu.getTitle(), 1);

        //TTL 확인
        Long expire = stringRedisTemplate.getExpire(key);
        if(expire != null && expire == -1L){
            redissonClient.getBucket(key).expire(7, TimeUnit.DAYS);
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
       redissonClient.getBucket(weeklyKey).expire(5, TimeUnit.MINUTES);

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
