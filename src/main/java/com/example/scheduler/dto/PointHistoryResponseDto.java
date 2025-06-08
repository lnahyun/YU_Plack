package com.example.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PointHistoryResponseDto {
    private int changeAmount;
    private String reason;
    private LocalDateTime createdAt;
}
