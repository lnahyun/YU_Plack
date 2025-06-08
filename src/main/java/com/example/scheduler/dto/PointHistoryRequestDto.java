package com.example.scheduler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointHistoryRequestDto {
    private int changeAmount;
    private String reason;
}
