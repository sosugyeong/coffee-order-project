package com.example.coffeeorderproject.domain.member.entity;

import com.example.coffeeorderproject.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    private Integer amount;

    @Builder
    private Member(String name, String phoneNumber, Integer amount){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.amount = amount;
    }

}
