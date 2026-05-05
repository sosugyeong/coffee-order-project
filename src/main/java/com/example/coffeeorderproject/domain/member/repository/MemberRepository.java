package com.example.coffeeorderproject.domain.member.repository;

import com.example.coffeeorderproject.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
