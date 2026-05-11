package com.example.coffeeorderproject.global.aop;

import com.example.coffeeorderproject.global.annotation.Idempotent;
import com.example.coffeeorderproject.global.exception.BusinessException;
import com.example.coffeeorderproject.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class IdempotentAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(idempotent)")
    public Object checkIdempotency(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        // Header에서 Idempotency-Key 추출
        String idempotencyKey = request.getHeader("Idempotency-Key");
        if (!StringUtils.hasText(idempotencyKey)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        // Redis에 저장될 키
        String redisKey = "idempotent:" + idempotencyKey;
        RBucket<String> bucket = redissonClient.getBucket(redisKey);

        boolean isSet = bucket.trySet("processing", idempotent.ttlSeconds(), TimeUnit.SECONDS);
        
        if (!isSet) {
            throw new BusinessException(ErrorCode.DUPLICATE_REQUEST);
        }

        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            // 로직 수행 중 에러 발생 시, 재시도 가능하도록 Redis 키 즉시 삭제
            bucket.delete();
            throw e;
        }
    }
}
