package com.example.coffeeorderproject.domain.point.manager;

import com.example.coffeeorderproject.domain.member.entity.Member;
import com.example.coffeeorderproject.domain.member.repository.MemberRepository;
import com.example.coffeeorderproject.domain.point.dto.request.PointRequest;
import com.example.coffeeorderproject.domain.point.dto.response.PointResponse;
import com.example.coffeeorderproject.domain.point.entity.Point;
import com.example.coffeeorderproject.domain.point.enums.PointType;
import com.example.coffeeorderproject.domain.point.repository.PointRepository;
import com.example.coffeeorderproject.global.exception.BusinessException;
import com.example.coffeeorderproject.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PointIncreaseManager {

    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;

    @Transactional
    public PointResponse executeCharge(PointRequest request) {
        Member member = memberRepository.findByUserIdentifier(request.userIdentifier())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Point pointCharge = Point.builder()
                .member(member)
                .point(request.point())
                .type(PointType.CHARGE)
                .build();

        member.chargePoint(request.point());
        pointRepository.save(pointCharge);
        return PointResponse.from(member, pointCharge);
    }
}
