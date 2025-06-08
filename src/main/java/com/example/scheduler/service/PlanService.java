package com.example.scheduler.service;

import com.example.scheduler.domain.Plan;
import com.example.scheduler.domain.User;
import com.example.scheduler.dto.PlanRequestDto;
import com.example.scheduler.repository.PlanRepository;
import com.example.scheduler.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    // 계획 작성
    @Transactional
    public Plan writePlan(Long userId, PlanRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        Plan plan = new Plan();
        plan.setContent(requestDto.getContent());
        plan.setType(Plan.PlanType.valueOf(requestDto.getType()));
        plan.setStartDate(LocalDate.parse(requestDto.getStartDate()));
        plan.setUser(user);

        return planRepository.save(plan);
    }

    // 계획 조회
    public List<Plan> viewPlans(Long userId, String type) {
        return planRepository.findByUserIdAndType(userId, Plan.PlanType.valueOf(type));
    }

    // 계획 수정
    @Transactional
    public Plan editPlan(Long planId, PlanRequestDto requestDto) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("해당 계획을 찾을 수 없습니다."));

        plan.setContent(requestDto.getContent());
        plan.setStartDate(LocalDate.parse(requestDto.getStartDate()));
        plan.setType(Plan.PlanType.valueOf(requestDto.getType()));
        return plan;
    }

    // 계획 삭제
    @Transactional
    public void deletePlan(Long planId) {
        planRepository.deleteById(planId);
    }
}
