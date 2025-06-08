package com.example.scheduler.controller;

import com.example.scheduler.dto.PointHistoryRequestDto;
import com.example.scheduler.dto.PointHistoryResponseDto;
import com.example.scheduler.service.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
public class PointHistoryController {

    private final PointHistoryService pointHistoryService;

    // 포인트 내역 등록
    @PostMapping("/{userId}")
    public ResponseEntity<Void> savePointHistory(@PathVariable Long userId,
                                                 @RequestBody PointHistoryRequestDto requestDto) {
        pointHistoryService.savePointHistory(userId, requestDto);
        return ResponseEntity.ok().build();
    }

    // 포인트 내역 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<PointHistoryResponseDto>> getUserPointHistories(@PathVariable Long userId) {
        List<PointHistoryResponseDto> response = pointHistoryService.getUserPointHistories(userId);
        return ResponseEntity.ok(response);
    }
}
