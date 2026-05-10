package com.example.coffeeorderproject.global.aop;

import com.example.coffeeorderproject.global.annotation.Idempotent;
import com.example.coffeeorderproject.global.exception.BusinessException;
import com.example.coffeeorderproject.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;

@Aspect
@Component
@RequiredArgsConstructor
public class IdempotentAspect {

    private final StringRedisTemplate redisTemplate;

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

        Boolean isSet = redisTemplate.opsForValue().setIfAbsent(redisKey, "processing", Duration.ofSeconds(idempotent.ttlSeconds()));
        if (Boolean.FALSE.equals(isSet)) {
            throw new BusinessException(ErrorCode.DUPLICATE_REQUEST);
        }

        return joinPoint.proceed();
    }
}
