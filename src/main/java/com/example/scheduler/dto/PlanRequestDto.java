package com.example.scheduler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanRequestDto {
    private String content;
    private String type;
    private String startDate;
}

