package com.example.coffeeorderproject.domain.member.entity;

import com.example.coffeeorderproject.global.common.BaseEntity;
import com.example.coffeeorderproject.global.exception.BusinessException;
import com.example.coffeeorderproject.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userIdentifier;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Builder.Default
    private Long amount = 0L;

    //포인트 충전
    public void chargePoint(Long point){
        if(point <= 0){
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        this.amount += point;
    }

    //포인트 차감
    public void usedPoint(Long point){
        if(point <= 0){
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        if(this.amount < point){
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        this.amount -= point;
    }
}
