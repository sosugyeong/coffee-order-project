package com.example.coffeeorderproject.domain.point.service;

import com.example.coffeeorderproject.domain.member.entity.Member;
import com.example.coffeeorderproject.domain.member.repository.MemberRepository;
import com.example.coffeeorderproject.domain.point.dto.request.PointRequest;
import com.example.coffeeorderproject.domain.point.dto.response.PointResponse;
import com.example.coffeeorderproject.domain.point.entity.Point;
import com.example.coffeeorderproject.domain.point.enums.PointType;
import com.example.coffeeorderproject.domain.point.manager.PointIncreaseManager;
import com.example.coffeeorderproject.domain.point.repository.PointRepository;
import com.example.coffeeorderproject.global.common.DistributedLockManager;
import com.example.coffeeorderproject.global.exception.BusinessException;
import com.example.coffeeorderproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;
    private final DistributedLockManager lockManager;

    // 락 키 prefix: 사용자별로 독립적인 락을 생성
    private static final String POINT_LOCK_KEY = "point:charge:";
    private final PointIncreaseManager pointIncreaseManager;

    public PointResponse chargePoint(PointRequest request) {
        // userIdentifier 기반으로 사용자별 분산 락 적용
        String lockKey = POINT_LOCK_KEY + request.userIdentifier();

        return lockManager.executeWithLock(lockKey, () -> {
            return pointIncreaseManager.executeCharge(request);
        });
    }
}
