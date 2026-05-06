package com.example.coffeeorderproject.domain.point.dto.response;

import com.example.coffeeorderproject.domain.member.entity.Member;
import com.example.coffeeorderproject.domain.point.entity.Point;
import com.example.coffeeorderproject.domain.point.enums.PointType;
import lombok.Builder;

@Builder
public record PointResponse(
        String memberName,
        Long point, //충전할 포인트
        Long amount, //포인트 총액
        PointType type
) {
    public static PointResponse from(Member member, Point point){
        return PointResponse.builder()
                .memberName(member.getName())
                .point(point.getPoint())
                .amount(member.getAmount())
                .type(point.getType())
                .build();
    }
}
