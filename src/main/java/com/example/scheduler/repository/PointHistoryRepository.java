package com.example.scheduler.repository;

import com.example.scheduler.domain.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
    List<PointHistory> findByUserId(Long userId);
}
