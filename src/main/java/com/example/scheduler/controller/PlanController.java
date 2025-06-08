package com.example.scheduler.controller;

import com.example.scheduler.domain.Plan;
import com.example.scheduler.dto.PlanRequestDto;
import com.example.scheduler.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans")
public class PlanController {

    private final PlanService planService;

    // 계획 작성
    @PostMapping("/{userId}")
    public ResponseEntity<Plan> writePlan(@PathVariable Long userId, @RequestBody PlanRequestDto requestDto) {
        Plan plan = planService.writePlan(userId, requestDto);
        return ResponseEntity.ok(plan);
    }

    // 계획 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<Plan>> getPlans(@PathVariable Long userId, @RequestParam String type) {
        List<Plan> plans = planService.viewPlans(userId, type);
        return ResponseEntity.ok(plans);
    }

    // 계획 수정
    @PutMapping("/{planId}")
    public ResponseEntity<Plan> updatePlan(@PathVariable Long planId, @RequestBody PlanRequestDto requestDto) {
        Plan updatedPlan = planService.editPlan(planId, requestDto);
        return ResponseEntity.ok(updatedPlan);
    }

    // 계획 삭제
    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long planId) {
        planService.deletePlan(planId);
        return ResponseEntity.noContent().build();
    }
}
