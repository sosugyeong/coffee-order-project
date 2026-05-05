package com.example.coffeeorderproject.domain.point.repository;

import com.example.coffeeorderproject.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
