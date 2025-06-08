package com.example.scheduler.dto;

import com.example.scheduler.domain.Plan;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanResponseDto {
    private Long id; // 저장된 계획의 고유 ID
    private String content;
    private String type;
    private String startDate;

    public PlanResponseDto(Plan plan) {
        this.id = plan.getId();
        this.content = plan.getContent();
        this.type = plan.getType().name();
        this.startDate = plan.getStartDate().toString();
    }
}
