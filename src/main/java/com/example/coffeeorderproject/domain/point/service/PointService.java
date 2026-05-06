package com.example.coffeeorderproject.domain.point.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public PointResponse chargePoint(PointRequest request){
        Member member = memberRepository.findByUserIdentifier(request.userIdentifier())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND)
        );

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
