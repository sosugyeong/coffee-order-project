package com.example.coffeeorderproject.global.common;

import com.example.coffeeorderproject.global.exception.BusinessException;
import com.example.coffeeorderproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class DistributedLockManager {

    private final RedissonClient redissonClient;

    // 대기 시간: 락획득을 최대 몇초까지 기다릴지
    private static final long WAIT_TIME = 5L;
    // 임대 시간: 락을 획득한 후 최대 몇초동안 유지할지 (데드락 방지)
    private static final long LEASE_TIME = 3L;

    // 분산락을 획득하고 성공시 주어진 작업을 실행한 후 락을 해제한다.
    // supplier: 락의 보호 하에 실행할 비즈니스 로직
    public <T> T executeWithLock(String lockKey, Supplier<T> supplier) {
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 락 획득 시도 (최대 WAIT_TIME초 대기, LEASE_TIME초 후에 자동 해제)
            boolean isLocked = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
            if (!isLocked) {
                log.warn("[DistributedLock] 락 획득 실패: key={}", lockKey);
                throw new BusinessException(ErrorCode.LOCK_ACQUISITION_FAILED);
            }

            log.info("[DistributedLock] 락 획득 성공: key={}", lockKey);
            return supplier.get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.LOCK_ACQUISITION_FAILED);
        } finally {
            // 락을 현재 스레드가 보유하고 있는 경우에만 해제
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("[DistributedLock] 락 해제: key={}", lockKey);
            }
        }
    }
}
