package com.example.scheduler.repository;

import com.example.scheduler.domain.Plan;
import com.example.scheduler.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByUserIdAndType(Long userId, Plan.PlanType type);
    //이거 사용되는지 확인해볼것
    boolean existsByUserAndStartDate(User user, java.time.LocalDate date);

}
